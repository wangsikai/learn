package com.lanking.uxb.service.diagnostic.api.student.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDiagnoRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClass;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassLatestHomework;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassLatestHomeworkKnowpoint;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassTextbook;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStatistic;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.diagnostic.api.student.StaticStuDiagnosticClassLatestHomeworkService;
import com.lanking.uxb.service.diagnostic.form.DiagnosticStudentClassForm;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;

@Transactional(readOnly = true)
@Service
public class StaticStuDiagnosticClassLatestHomeworkServiceImpl
		implements StaticStuDiagnosticClassLatestHomeworkService {

	private Logger logger = LoggerFactory.getLogger(StaticStuDiagnosticClassLatestHomeworkServiceImpl.class);
	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	private Repo<HomeworkStudentClazz, Long> homeworkStudentClazzRepo;

	@Autowired
	@Qualifier("DiagnosticStudentClassLatestHomeworkRepo")
	private Repo<DiagnosticStudentClassLatestHomework, Long> diagnosticStuClassLatestHkRepo;

	@Autowired
	@Qualifier("DiagnosticStudentClassRepo")
	private Repo<DiagnosticStudentClass, Long> diagnosticStudentClassRepo;

	@Autowired
	@Qualifier("DiagnosticStudentClassLatestHomeworkKnowpointRepo")
	private Repo<DiagnosticStudentClassLatestHomeworkKnowpoint, Long> diagnosticStudentClassLatestHomeworkKnowpointRepo;

	@Autowired
	@Qualifier("StudentHomeworkRepo")
	private Repo<StudentHomework, Long> stuHomeworkRepo;

	@Autowired
	@Qualifier("StudentHomeworkQuestionRepo")
	private Repo<StudentHomeworkQuestion, Long> studentHomeworkQuestionRepo;

	@Autowired
	@Qualifier("HomeworkRepo")
	private Repo<Homework, Long> homeworkRepo;

	@Autowired
	@Qualifier("QuestionRepo")
	private Repo<Question, Long> questionRepo;

	@Autowired
	@Qualifier("HomeworkClazzRepo")
	Repo<HomeworkClazz, Long> homeworkClazzRepo;

	@Autowired
	@Qualifier("TeacherRepo")
	private Repo<Teacher, Long> teacherRepo;

	@Autowired
	@Qualifier("DiagnosticStudentClassTextbookRepo")
	private Repo<DiagnosticStudentClassTextbook, Long> diagnosticStudentClassTextbookRepo;

	@Autowired
	@Qualifier("StudentHomeworkStatisticRepo")
	private Repo<StudentHomeworkStatistic, Long> studentHomeworkStatisticRepo;

	@Autowired
	private KnowledgeSectionService knowledgeSectionService;

	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private ZyHomeworkStudentClazzService hkStuClazzService;

	@Override
	@Transactional(readOnly = true)
	public void statStuClassLastestData(long classId, Long hkId) {
		// 查询班级对应的学生
		List<Long> studentList = homeworkStudentClazzRepo.find("$taskListStudent", Params.param("classId", classId))
				.list(Long.class);
		for (Long studentId : studentList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("studentId", studentId);
			jsonObject.put("classId", classId);
			// TODO 暂时去除 2018-03-07，后面需要根据新流程开启
			mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
					MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK + "." + (studentId % 11),
					MQ.builder().data(jsonObject).build());
		}
	}

	@Transactional
	@Override
	public void statOneStuClassLastestData(long studentId, long classId) {
		// this.deleteLastestByStuClass(classId, studentId);
		List<Map> stuHkList = this.queryCommonHkList(studentId, classId);
		List<Long> stuHkIds = new ArrayList<Long>();
		if (CollectionUtils.isNotEmpty(stuHkList)) {
			for (Map stuHk : stuHkList) {
				Long homeworkId = Long.parseLong(stuHk.get("homework_id").toString());
				Long stuHomeworkId = Long.parseLong(stuHk.get("id").toString());
				int rank = Integer.parseInt(stuHk.get("rank").toString());
				BigDecimal rightRate = (BigDecimal) stuHk.get("right_rate");
				stuHkIds.add(stuHomeworkId);
				this.statStuClassLatestHk(studentId, homeworkId, rank, rightRate);
			}
			this.statStuClassLatestByTextbook(stuHkIds, studentId, classId);
			this.statStuClassLatestHkKp(stuHkIds, studentId, classId);
		}
	}

	@Override
	@Transactional
	public void statStuClassLastestDataByStuId(long studentId, Long classId) {
		List<Map> stuHkList = this.queryCommonHkList(studentId, classId);
		List<Long> stuHkIds = new ArrayList<Long>();
		if (CollectionUtils.isNotEmpty(stuHkList)) {
			for (Map stuHk : stuHkList) {
				Long homeworkId = Long.parseLong(stuHk.get("homework_id").toString());
				Long stuHomeworkId = Long.parseLong(stuHk.get("id").toString());
				int rank = Integer.parseInt(stuHk.get("rank").toString());
				BigDecimal rightRate = (BigDecimal) stuHk.get("right_rate");
				stuHkIds.add(stuHomeworkId);
				this.statStuClassLatestHk(studentId, homeworkId, rank, rightRate);
			}
			this.statStuClassLatestByTextbook(stuHkIds, studentId, classId);
			this.statStuClassLatestHkKp(stuHkIds, studentId, classId);
		}

	}

	@Override
	@Transactional
	public void statStuClassLatestHk(long studentId, long homeworkId, Integer rank, BigDecimal rightRate) {
		DiagnosticStudentClassLatestHomework d = this.getDiagnosticStuClassLatestHk(studentId, homeworkId);
		if (d == null) {
			d = new DiagnosticStudentClassLatestHomework();
			Homework homework = homeworkRepo.get(homeworkId);
			d.setClassId(homework.getHomeworkClassId());
			d.setClassRightRate(homework.getRightRate());
			d.setStudentId(studentId);
			d.setDifficulty(homework.getDifficulty());
			d.setHomeworkId(homework.getId());
			d.setName(homework.getName());
			d.setRank(rank);
			d.setRightRate(rightRate);
			d.setStartTime(homework.getStartTime());
			diagnosticStuClassLatestHkRepo.save(d);
		}

	}

	@Override
	public void statStuClassLatestHkKp(List<Long> stuHkIds, long studentId, long classId) {
		List<StudentHomeworkQuestion> list = studentHomeworkQuestionRepo
				.find("$findQuestions", Params.param("stuHkIds", stuHkIds)).list();
		List<Long> stuHkIds_15 = new ArrayList<Long>();
		List<Long> stuHkIds_7 = new ArrayList<Long>();
		stuHkIds_15 = stuHkIds.size() >= 15 ? stuHkIds.subList(0, 15) : stuHkIds;
		stuHkIds_7 = stuHkIds.size() >= 7 ? stuHkIds.subList(0, 7) : stuHkIds;
		for (StudentHomeworkQuestion stuq : list) {
			this.lastToSave(classId, studentId, stuq.getQuestionId(), stuq.getResult(), 30);
			if (stuHkIds_15.contains(stuq.getStudentHomeworkId())) {
				this.lastToSave(classId, studentId, stuq.getQuestionId(), stuq.getResult(), 15);
			}
			if (stuHkIds_7.contains(stuq.getStudentHomeworkId())) {
				this.lastToSave(classId, studentId, stuq.getQuestionId(), stuq.getResult(), 7);
			}
		}

	}

	@Override
	public void statStuClassLatestByTextbook(List<Long> stuHkIds, long studentId, long classId) {
		Teacher teacher = null;
		Set<Integer> inTextbookCodes = new HashSet<Integer>();
		Long teacherId = homeworkClazzRepo.get(classId).getTeacherId();
		if (teacherId != null) {
			teacher = teacherRepo.get(teacherId);

			List<StudentHomeworkQuestion> list = studentHomeworkQuestionRepo
					.find("$findQuestions", Params.param("stuHkIds", stuHkIds)).list();
			for (StudentHomeworkQuestion question : list) {
				List<Long> codes = questionKnowledgeService.queryKnowledgeByQuestionId(question.getQuestionId());
				if (CollectionUtils.isEmpty(codes)) {
					continue;
				}
				List<Integer> textbookCodes = knowledgeSectionService.queryTextbookByKnowledge(codes);
				Question q = questionRepo.get(question.getQuestionId());
				boolean inTeacherCategory = false;
				for (Integer tCode : textbookCodes) {
					if (tCode.toString().startsWith(teacher.getTextbookCategoryCode().toString())) {
						inTeacherCategory = true;
						inTextbookCodes.add(tCode);
						DiagnosticStudentClassForm form = this.getStuClassForm(classId, studentId, tCode,
								question.getResult(), q);
						this.saveStuClass(form);
					}
				}
				// 当题目当前对应的知识点并不在老师当前的版本下面则不计入统计
				if (!inTeacherCategory) {
					continue;
				}
			}

			this.statStuClassTextbook(inTextbookCodes, classId, studentId);
		}
	}

	@Transactional
	@Override
	public void statStuClassTextbook(Set<Integer> inTextbookCodes, long classId, long studentId) {
		for (Integer code : inTextbookCodes) {
			Params params = Params.param("clazzId", classId);
			params.put("textbookCode", code);
			params.put("studentId", studentId);
			DiagnosticStudentClassTextbook dt = diagnosticStudentClassTextbookRepo
					.find("$taskGetStuClassTextbook", params).get();
			if (dt == null) {
				dt = new DiagnosticStudentClassTextbook();
				dt.setClassId(classId);
				dt.setStudentId(studentId);
				dt.setTextbookCode(code);
				dt.setUpdateAt(new Date());
				dt.setCreateAt(new Date());
				diagnosticStudentClassTextbookRepo.save(dt);
			}
		}
	}

	@Transactional
	@Override
	public void statWhenJoin(long studentId, Long classId) {
		// 删除历史数据
		this.deleteLastestByStuClass(classId, studentId);
		// 重新跑一下最新30次这个学生班级的数据
		this.statStuClassLastestDataByStuId(studentId, classId);
	}

	@Transactional
	@Override
	public void statWhenLeave(long studentId, Long classId) {
		this.deleteLastestByStuClass(classId, studentId);
	}

	@Transactional
	public void saveStuClass(DiagnosticStudentClassForm form) {
		Params params = Params.param("classId", form.getClassId());
		params.put("studentId", form.getStudentId());
		params.put("textbookCode", form.getTextbookCode());
		DiagnosticStudentClass point = diagnosticStudentClassRepo.find("$taskGetStuClass", params).get();
		if (point == null) {
			point = new DiagnosticStudentClass();
			point.setTextbookCode(form.getTextbookCode());
			point.setClassId(form.getClassId());
			point.setStudentId(form.getStudentId());
			point.setDoHard1Count(form.getDoHard1Count());
			point.setRightHard1Count(form.getRightHard1Count());
			point.setDoHard2Count(form.getDoHard2Count());
			point.setRightHard2Count(form.getRightHard2Count());
			point.setDoHard3Count(form.getDoHard3Count());
			point.setRightHard3Count(form.getRightHard3Count());
			point.setDoCountMonth(point.getDoHard1Count() + point.getDoHard2Count() + point.getDoHard3Count());
			point.setRightCountMonth(
					point.getRightHard1Count() + point.getRightHard2Count() + point.getRightHard3Count());
			point.setCreateAt(new Date());
		} else {
			point.setUpdateAt(new Date());
			point.setDoHard1Count(form.getDoHard1Count() + point.getDoHard1Count());
			point.setRightHard1Count(form.getRightHard1Count() + point.getRightHard1Count());
			point.setDoHard2Count(form.getDoHard2Count() + point.getDoHard2Count());
			point.setRightHard2Count(form.getRightHard2Count() + point.getRightHard2Count());
			point.setDoHard3Count(form.getDoHard3Count() + point.getDoHard3Count());
			point.setRightHard3Count(form.getRightHard3Count() + point.getRightHard3Count());
			point.setDoCountMonth(point.getDoHard1Count() + point.getDoHard2Count() + point.getDoHard3Count());
			point.setRightCountMonth(
					point.getRightHard1Count() + point.getRightHard2Count() + point.getRightHard3Count());
		}
		diagnosticStudentClassRepo.save(point);
	}

	public DiagnosticStudentClassForm getStuClassForm(Long classId, Long studentId, int code,
			HomeworkAnswerResult result, Question q) {
		DiagnosticStudentClassForm form = new DiagnosticStudentClassForm();
		form.setClassId(classId);
		form.setStudentId(studentId);
		form.setTextbookCode(code);
		int rightCount = result == HomeworkAnswerResult.RIGHT ? 1 : 0;
		if (q.getDifficulty() >= 0 && q.getDifficulty() < 0.4) {
			// 冲刺题
			form.setDoHard3Count(1);
			form.setRightHard3Count(rightCount);

		} else if (q.getDifficulty() >= 0.4 && q.getDifficulty() < 0.8) {
			// 提高题
			form.setDoHard2Count(1);
			form.setRightHard2Count(rightCount);
		} else if (q.getDifficulty() >= 0.8 && q.getDifficulty() <= 1) {
			// 基础题
			form.setDoHard1Count(1);
			form.setRightHard1Count(rightCount);
		}
		return form;
	}

	/**
	 * 实时知识点数据
	 * 
	 * @param classId
	 * @param studentId
	 * @param questionIds
	 * @param questionResultMap
	 */
	public void lastToSave(Long classId, Long studentId, Long questionId, HomeworkAnswerResult result, Integer times) {
		// 做了多少题
		Map<Long, Integer> doCountMap = new HashMap<Long, Integer>();
		// 做对了多少题
		Map<Long, Integer> rightCountMap = new HashMap<Long, Integer>();
		Set<Long> allCodes = new HashSet<Long>();

		List<Long> codes = questionKnowledgeService.queryKnowledgeByQuestionId(questionId);
		if (CollectionUtils.isEmpty(codes)) {
			return;
		}
		// 查出所有不重复的父类集合
		List<Long> parents = questionKnowledgeService.queryParentKnowledgeCodes(codes);
		codes.addAll(parents);
		// 知识点和所有的父类，一道题只算一个
		for (Long code : codes) {
			// 做题数
			doCountMap.put(code, doCountMap.get(code) == null ? 1 : doCountMap.get(code) + 1);
			if (result == HomeworkAnswerResult.RIGHT) {
				rightCountMap.put(code, rightCountMap.get(code) == null ? 1 : rightCountMap.get(code) + 1);
			}
		}
		allCodes.addAll(codes);

		if (CollectionUtils.isEmpty(allCodes)) {
			return;
		}
		for (Long code : allCodes) {
			this.saveStuClassLatestHkKpt(studentId, code, doCountMap.get(code),
					rightCountMap.get(code) == null ? 0 : rightCountMap.get(code), classId, times);
		}
	}

	/**
	 * 删除之前统计的30次数据
	 * 
	 * @param classId
	 */
	@Transactional
	public void deleteLastestByStuClass(long classId, Long studentId) {
		Params params = Params.param("classId", classId);
		if (studentId != null) {
			params.put("studentId", studentId);
		}
		diagnosticStudentClassLatestHomeworkKnowpointRepo.execute("$taskDelStuClassLastestHkKp", params);
		diagnosticStuClassLatestHkRepo.execute("$taskDelStuClassLatestHk", params);
		diagnosticStudentClassRepo.execute("$taskDelStuClass", params);
	}

	@Transactional
	public void saveStuClassLatestHkKpt(Long studentId, Long knowpointCode, int doCount, int rightCount, Long clazzId,
			int times) {
		DiagnosticStudentClassLatestHomeworkKnowpoint point = this.find(clazzId, studentId, knowpointCode, times);
		if (point == null) {
			point = new DiagnosticStudentClassLatestHomeworkKnowpoint();
			point.setKnowpointCode(knowpointCode);
			point.setDoCount(doCount);
			point.setRightCount(rightCount);
			point.setStudentId(studentId);
			point.setClassId(clazzId);
			// 新增统计数据是来自最近7,15,30次的区别
			point.setTimes(times);
		} else {
			point.setDoCount(point.getDoCount() + doCount);
			point.setRightCount(point.getRightCount() + rightCount);
		}
		if (point.getDoCount() > 0) {
			point.setRightRate(new BigDecimal(point.getRightCount() * 100d / point.getDoCount()).setScale(2,
					BigDecimal.ROUND_HALF_UP));
		} else {
			point.setRightRate(BigDecimal.valueOf(0));
		}
		diagnosticStudentClassLatestHomeworkKnowpointRepo.save(point);
	}

	public DiagnosticStudentClassLatestHomeworkKnowpoint find(Long classId, Long studentId, Long knowpointCode,
			int times) {
		return diagnosticStudentClassLatestHomeworkKnowpointRepo
				.find("$taskGetStuClassLatestHkKp", Params.param("studentId", studentId)
						.put("knowpointCode", knowpointCode).put("classId", classId).put("times", times))
				.get();
	}

	/**
	 * 查询普通作业列表
	 * 
	 * @param studentId
	 * @param classId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> queryCommonHkList(Long studentId, Long classId) {
		Params params = Params.param("studentId", studentId).put("classId", classId);
		return stuHomeworkRepo.find("$queryCommonHkList", params).list(Map.class);
	}

	public DiagnosticStudentClassLatestHomework getDiagnosticStuClassLatestHk(Long studentId, Long homeworkId) {
		return diagnosticStuClassLatestHkRepo.find("$getDiagnosticStuClassLatestHk",
				Params.param("studentId", studentId).put("homeworkId", homeworkId)).get();
	}

	@Transactional
	@SuppressWarnings("rawtypes")
	@Override
	public void stuHkStatistic(Long homeworkId) {
		Homework homework = homeworkRepo.get(homeworkId);
		if (homework == null) {
			return;
		}
		List<Map> list = this.stuHkStat(homework.getHomeworkClassId());
		// 只统计已下发的正确率
		Map<Long, Integer> rateMap = this.rateMap(homework.getHomeworkClassId());
		for (Map map : list) {
			Long userId = Long.parseLong(String.valueOf(map.get("student_id")));
			StudentHomeworkStatistic ss = studentHomeworkStatisticRepo.get(userId);
			if (ss == null) {
				ss = new StudentHomeworkStatistic();
				ss.setUserId(userId);
			}
			Long homeworkCount = Long.parseLong(String.valueOf(map.get("homework_num")));
			ss.setHomeWorkCount(homeworkCount);
			if (rateMap.get(userId) != null) {
				ss.setRightRate(BigDecimal.valueOf(rateMap.get(userId)));
			}
			if (map.get("completion_rate") != null) {
				Double completionRate = Double.parseDouble(String.valueOf(map.get("completion_rate")));
				ss.setCompletionRate(BigDecimal.valueOf(completionRate));
			}
			studentHomeworkStatisticRepo.save(ss);
		}
	}

	@SuppressWarnings("rawtypes")
	@Transactional
	@Override
	public void initStuHkStatistic(List<Long> studentIds) {
		List<Map> list = this.stuHkStatByStudentIds(studentIds);
		Map<Long, Integer> rateMap = this.rateMapByStudentIds(studentIds);
		for (Map map : list) {
			StudentHomeworkStatistic ss = new StudentHomeworkStatistic();
			Long userId = Long.parseLong(String.valueOf(map.get("student_id")));
			Long homeworkCount = Long.parseLong(String.valueOf(map.get("homework_num")));
			if (rateMap.get(userId) != null) {
				ss.setRightRate(BigDecimal.valueOf(rateMap.get(userId)));
			}
			if (map.get("completion_rate") != null) {
				Double completionRate = Double.parseDouble(String.valueOf(map.get("completion_rate")));
				ss.setCompletionRate(BigDecimal.valueOf(completionRate));
			}
			ss.setUserId(userId);
			ss.setHomeWorkCount(homeworkCount);
			studentHomeworkStatisticRepo.save(ss);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> stuHkStat(Long classId) {
		Params params = Params.param();
		if (classId != null) {
			params.put("classId", classId);
		}
		return studentHomeworkStatisticRepo.find("$stuHkStat", params).list(Map.class);
	}

	@Override
	public Map<Long, Integer> rateMap(Long classId) {
		Params params = Params.param();
		if (classId != null) {
			params.put("classId", classId);
		}
		List<Map> list = studentHomeworkStatisticRepo.find("$stuRateStat", params).list(Map.class);
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		for (Map pa : list) {
			Long studentId = Long.parseLong(String.valueOf(pa.get("student_id")));
			if (pa.get("right_rate") != null) {
				Integer rightRate = Integer.parseInt(String.valueOf(pa.get("right_rate")));
				map.put(studentId, rightRate);
			}
		}
		return map;
	}

	@Override
	public List<Map> stuHkStatByStudentIds(List<Long> studentIds) {
		return studentHomeworkStatisticRepo.find("$stuHkStatByStudentIds", Params.param("studentIds", studentIds))
				.list(Map.class);
	}

	@Override
	public Map<Long, Integer> rateMapByStudentIds(List<Long> studentIds) {
		Params params = Params.param();
		params.put("studentIds", studentIds);
		List<Map> list = studentHomeworkStatisticRepo.find("$rateMapByStudentIds", params).list(Map.class);
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		for (Map pa : list) {
			Long studentId = Long.parseLong(String.valueOf(pa.get("student_id")));
			if (pa.get("right_rate") != null) {
				Integer rightRate = Integer.parseInt(String.valueOf(pa.get("right_rate")));
				map.put(studentId, rightRate);
			}

		}
		return map;
	}
}
