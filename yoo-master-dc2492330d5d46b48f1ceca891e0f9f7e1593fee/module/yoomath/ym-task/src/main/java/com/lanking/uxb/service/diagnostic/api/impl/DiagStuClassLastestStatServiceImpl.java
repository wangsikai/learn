package com.lanking.uxb.service.diagnostic.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClass;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassKnowpoint;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassLatestHomework;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassLatestHomeworkKnowpoint;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassTextbook;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.diagnostic.api.DiagStuClassLastestStatService;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkClassService;
import com.lanking.uxb.service.diagnostic.form.DiagnosticStudentClassForm;
import com.lanking.uxb.service.diagnostic.form.DiagnosticStudentClassKnowpointForm;

@Transactional(readOnly = true)
@Service
public class DiagStuClassLastestStatServiceImpl implements DiagStuClassLastestStatService {

	@Autowired
	@Qualifier("HomeworkRepo")
	private Repo<Homework, Long> homeworkRepo;

	@Autowired
	@Qualifier("TeacherRepo")
	private Repo<Teacher, Long> teacherRepo;

	@Autowired
	@Qualifier("QuestionRepo")
	private Repo<Question, Long> questionRepo;

	@Autowired
	@Qualifier("HolidayHomeworkRepo")
	private Repo<HolidayHomework, Long> holidayHomeworkRepo;

	@Autowired
	@Qualifier("StudentHomeworkRepo")
	private Repo<StudentHomework, Long> stuHomeworkRepo;

	@Autowired
	@Qualifier("StudentRepo")
	private Repo<Student, Long> studentRepo;

	@Autowired
	@Qualifier("DiagnosticStudentClassLatestHomeworkRepo")
	private Repo<DiagnosticStudentClassLatestHomework, Long> diagnosticStuClassLatestHkRepo;

	@Autowired
	@Qualifier("StudentHomeworkQuestionRepo")
	private Repo<StudentHomeworkQuestion, Long> studentHomeworkQuestionRepo;

	@Autowired
	@Qualifier("DiagnosticStudentClassLatestHomeworkKnowpointRepo")
	private Repo<DiagnosticStudentClassLatestHomeworkKnowpoint, Long> diagnosticStudentClassLatestHomeworkKnowpointRepo;

	@Autowired
	@Qualifier("HolidayStuHomeworkItemQuestionRepo")
	private Repo<HolidayStuHomeworkItemQuestion, Long> holidayStuHomeworkItemQuestionRepo;

	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	private Repo<HomeworkStudentClazz, Long> homeworkStudentClazzRepo;

	@Autowired
	@Qualifier("DiagnosticStudentClassKnowpointRepo")
	private Repo<DiagnosticStudentClassKnowpoint, Long> diagnosticStudentClassKnowpointRepo;

	@Autowired
	@Qualifier("DiagnosticStudentClassRepo")
	private Repo<DiagnosticStudentClass, Long> diagnosticStudentClassRepo;

	@Autowired
	@Qualifier("DiagnosticStudentClassTextbookRepo")
	private Repo<DiagnosticStudentClassTextbook, Long> diagnosticStudentClassTextbookRepo;

	@Autowired
	@Qualifier("HomeworkClazzRepo")
	Repo<HomeworkClazz, Long> homeworkClazzRepo;
	private static final int CLASS_FETCH_SIZE = 20;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private KnowledgeSectionService knowledgeSectionService;
	@Autowired
	private StaticHomeworkClassService homeworkClassService;

	/**
	 * 统计diagno_stu_class_latest_hk、diagno_stu_class_latest_hk_kp
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	@Override
	public void statStuClassLastestData(Long classId, Long hkId) {
		// 查询班级对应的
		List<Long> studentList = homeworkStudentClazzRepo.find("$taskListStudent", Params.param("classId", classId))
				.list(Long.class);
		// 每次下发会删除之前30次的统计
		this.deleteLastestByStuClass(classId);
		for (Long studentId : studentList) {
			// 只有普通作业
			// 需求变更 2017.2.24跟UE沟通，改为只取普通作业的
			List<Map> stuHkList = this.queryCommonHkList(studentId, classId);
			List<Long> stuHkIds = new ArrayList<Long>();
			if (CollectionUtils.isNotEmpty(stuHkList)) {
				// 只取普通作业
				for (Map stuHk : stuHkList) {
					Long homeworkId = Long.parseLong(stuHk.get("homework_id").toString());
					Long stuHomeworkId = Long.parseLong(stuHk.get("id").toString());
					int rank = Integer.parseInt(stuHk.get("rank").toString());
					BigDecimal rightRate = (BigDecimal) stuHk.get("right_rate");
					this.saveStuClassLatestHk(studentId, homeworkId, false, rank, rightRate);
					stuHkIds.add(stuHomeworkId);
				}
				this.statAboutTextbook(classId, stuHkIds, studentId, false);
				this.dealCommonStuClassLastestHkpt(stuHkIds, studentId, classId);
			}
		}
	}

	@Transactional
	public void deleteLastestByStuClass(Long classId) {
		Params params = Params.param("classId", classId);
		// 根据学生和班级Id,删除最新班级作业知识点
		diagnosticStudentClassLatestHomeworkKnowpointRepo.execute("$taskDelStuClassLastestHkKp", params);
		diagnosticStuClassLatestHkRepo.execute("$taskDelStuClassLatestHk", params);
		diagnosticStudentClassRepo.execute("$taskDelStuClass", params);
	}

	/**
	 * 保存diagno_stu_class_latest_hk(只有普通作业)
	 * 
	 * @param studentId
	 * @param homeworkId
	 * @param isHoliday
	 * @param rank
	 * @param rightRate
	 */
	public void saveStuClassLatestHk(Long studentId, Long homeworkId, Boolean isHoliday, int rank,
			BigDecimal rightRate) {
		DiagnosticStudentClassLatestHomework d = this.getDiagnosticStuClassLatestHk(studentId, homeworkId);
		if (d == null) {
			d = new DiagnosticStudentClassLatestHomework();
			if (!isHoliday) {
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
			} else {
				HolidayHomework homework = holidayHomeworkRepo.get(homeworkId);
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
	}

	/**
	 * 统计diagno_stu_class_kp(包括普通作业和假期作业)
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	@Override
	public void statStuClassData(Date startTime, Date endTime, Long clazzId) {
		List<Map> stuHkList = this.queryLatestList(startTime, endTime, clazzId);
		// 学生和questionId的关系
		Map<Long, List<Long>> stuQuesMap = new HashMap<Long, List<Long>>();
		Map<Long, List<HomeworkAnswerResult>> stuResultMap = new HashMap<Long, List<HomeworkAnswerResult>>();
		if (CollectionUtils.isNotEmpty(stuHkList)) {
			for (Map stuHk : stuHkList) {
				Long studentId = Long.parseLong(stuHk.get("student_id").toString());
				Long questionId = Long.parseLong(stuHk.get("question_id").toString());
				Integer result = Integer.parseInt(stuHk.get("result").toString());
				if (stuQuesMap.containsKey(studentId)) {
					stuQuesMap.get(studentId).add(questionId);
				} else {
					List<Long> list = new ArrayList<Long>();
					list.add(questionId);
					stuQuesMap.put(studentId, list);
				}
				if (stuResultMap.containsKey(studentId)) {
					stuResultMap.get(studentId).add(HomeworkAnswerResult.findByValue(result));
				} else {
					List<HomeworkAnswerResult> list = new ArrayList<HomeworkAnswerResult>();
					list.add(HomeworkAnswerResult.findByValue(result));
					stuResultMap.put(studentId, list);
				}
			}
		}
		for (Long studentId : stuQuesMap.keySet()) {
			this.formToSave(clazzId, studentId, stuQuesMap.get(studentId), stuResultMap.get(studentId));
		}
	}

	/**
	 * 统计跟教材相关表的数据(diagno_stu_class/DiagnosticStudentClass)
	 */
	@Transactional
	public void statAboutTextbook(Long homeworkClassId, List<Long> stuHomeworkIds, Long studentId, Boolean isHoliday) {
		Teacher teacher = null;
		Set<Integer> inTextbookCodes = new HashSet<Integer>();
		Long teacherId = homeworkClazzRepo.get(homeworkClassId).getTeacherId();
		if (homeworkClazzRepo.get(homeworkClassId).getTeacherId() != null) {
			teacher = teacherRepo.get(teacherId);
			if (!isHoliday) {
				List<StudentHomeworkQuestion> list = studentHomeworkQuestionRepo
						.find("$findQuestions", Params.param("stuHkIds", stuHomeworkIds)).list();
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
							DiagnosticStudentClassForm form = this.getStuClassForm(homeworkClassId, studentId, tCode,
									question.getResult(), q);
							this.saveStuClass(form);
						}
					}
					// 当题目当前对应的知识点并不在老师当前的版本下面则不计入统计
					if (!inTeacherCategory) {
						continue;
					}

				}
			} else {
				List<HolidayStuHomeworkItemQuestion> list = holidayStuHomeworkItemQuestionRepo
						.find("$findHolidayQuestions", Params.param("holidayStudentHomeworkIds", stuHomeworkIds))
						.list();
				for (HolidayStuHomeworkItemQuestion question : list) {
					List<Long> codes = questionKnowledgeService.queryKnowledgeByQuestionId(question.getQuestionId());
					if (CollectionUtils.isEmpty(codes)) {
						continue;
					}
					Question q = questionRepo.get(question.getQuestionId());
					List<Integer> textbookCodes = knowledgeSectionService.queryTextbookByKnowledge(codes);

					boolean inTeacherCategory = false;
					for (Integer tCode : textbookCodes) {
						if (tCode.toString().startsWith(teacher.getTextbookCategoryCode().toString())) {
							inTeacherCategory = true;
							inTextbookCodes.add(tCode);
							DiagnosticStudentClassForm form = this.getStuClassForm(homeworkClassId, studentId, tCode,
									question.getResult(), q);
							this.saveStuClass(form);
						}
					}
					// 当题目当前对应的知识点并不在老师当前的版本下面则不计入统计
					if (!inTeacherCategory) {
						continue;
					}

				}
			}
			this.saveStuClassTextbook(homeworkClassId, inTextbookCodes, studentId);
		}
	}

	/**
	 * 保存diagno_class_textbook
	 * 
	 * @param clazzId
	 * @param textBooks
	 * @param studentId
	 */
	@Transactional
	public void saveStuClassTextbook(Long clazzId, Collection<Integer> textBooks, Long studentId) {
		for (Integer code : textBooks) {
			Params params = Params.param("clazzId", clazzId);
			params.put("textbookCode", code);
			params.put("studentId", studentId);
			DiagnosticStudentClassTextbook dt = diagnosticStudentClassTextbookRepo
					.find("$taskGetStuClassTextbook", params).get();
			if (dt == null) {
				dt = new DiagnosticStudentClassTextbook();
				dt.setClassId(clazzId);
				dt.setStudentId(studentId);
				dt.setTextbookCode(code);
				dt.setUpdateAt(new Date());
				dt.setCreateAt(new Date());
				diagnosticStudentClassTextbookRepo.save(dt);
			}
		}

	}

	/**
	 * 一次作业存在多个知识点重复，这里做汇总处理，key值为对应的code
	 * 
	 * @param classId
	 * @param studentId
	 * @param questionIds
	 *            题目集合
	 * @param questionResultMap
	 *            题目对应的对错集合
	 * @return
	 */
	public void formToSave(Long classId, Long studentId, List<Long> questionIds,
			List<HomeworkAnswerResult> questionResultList) {
		Map<Long, Question> questionMap = questionRepo.mget(questionIds);
		// 最大难度Map
		Map<Long, Double> maxDiffMap = new HashMap<Long, Double>();
		// 最小难度Map
		Map<Long, Double> minDiffMap = new HashMap<Long, Double>();
		// 做了多少题
		Map<Long, Integer> doCountMap = new HashMap<Long, Integer>();
		// 做对了多少题
		Map<Long, Integer> rightCountMap = new HashMap<Long, Integer>();
		// 不同难度做题的Map
		Map<Long, Integer> doHard1CountMap = new HashMap<Long, Integer>();
		Map<Long, Integer> doHard2CountMap = new HashMap<Long, Integer>();
		Map<Long, Integer> doHard3CountMap = new HashMap<Long, Integer>();
		// 不同难度做对题的Map
		Map<Long, Integer> rightHard1CountMap = new HashMap<Long, Integer>();
		Map<Long, Integer> rightHard2CountMap = new HashMap<Long, Integer>();
		Map<Long, Integer> rightHard3CountMap = new HashMap<Long, Integer>();
		Set<Long> allCodes = new HashSet<Long>();
		int i = 0;
		for (Long questionId : questionIds) {
			List<Long> codes = questionKnowledgeService.queryKnowledgeByQuestionId(questionId);
			if (CollectionUtils.isEmpty(codes)) {
				i++;
				continue;
			}
			Question ques = questionMap.get(questionId);
			HomeworkAnswerResult result = questionResultList.get(i);
			i++;
			// 查出所有不重复的父类集合
			List<Long> parents = questionKnowledgeService.queryParentKnowledgeCodes(codes);
			codes.addAll(parents);
			// 知识点和所有的父类，一道题只算一个
			for (Long code : codes) {
				// 最大难度
				if (maxDiffMap.get(code) == null) {
					maxDiffMap.put(code, ques.getDifficulty());
				} else {
					if (ques.getDifficulty() > maxDiffMap.get(code)) {
						maxDiffMap.put(code, ques.getDifficulty());
					}
				}
				// 最小难度
				if (minDiffMap.get(code) == null) {
					minDiffMap.put(code, ques.getDifficulty());
				} else {
					if (ques.getDifficulty() < minDiffMap.get(code)) {
						minDiffMap.put(code, ques.getDifficulty());
					}
				}
				// 做题数
				doCountMap.put(code, doCountMap.get(code) == null ? 1 : doCountMap.get(code) + 1);
				if (ques.getDifficulty() >= 0.0 && ques.getDifficulty() < 0.4) {
					doHard3CountMap.put(code, doHard3CountMap.get(code) == null ? 1 : doHard3CountMap.get(code) + 1);
					if (result == HomeworkAnswerResult.RIGHT) {
						rightHard3CountMap.put(code,
								rightHard3CountMap.get(code) == null ? 1 : rightHard3CountMap.get(code) + 1);
						rightCountMap.put(code, rightCountMap.get(code) == null ? 1 : rightCountMap.get(code) + 1);
					}
				} else if (ques.getDifficulty() >= 0.4 && ques.getDifficulty() < 0.8) {
					doHard2CountMap.put(code, doHard2CountMap.get(code) == null ? 1 : doHard2CountMap.get(code) + 1);
					if (result == HomeworkAnswerResult.RIGHT) {
						rightHard2CountMap.put(code,
								rightHard2CountMap.get(code) == null ? 1 : rightHard2CountMap.get(code) + 1);
						rightCountMap.put(code, rightCountMap.get(code) == null ? 1 : rightCountMap.get(code) + 1);
					}
				} else {
					doHard1CountMap.put(code, doHard1CountMap.get(code) == null ? 1 : doHard1CountMap.get(code) + 1);
					if (result == HomeworkAnswerResult.RIGHT) {
						rightHard1CountMap.put(code,
								rightHard1CountMap.get(code) == null ? 1 : rightHard1CountMap.get(code) + 1);
						rightCountMap.put(code, rightCountMap.get(code) == null ? 1 : rightCountMap.get(code) + 1);
					}
				}
			}
			allCodes.addAll(codes);
		}
		if (CollectionUtils.isEmpty(allCodes)) {
			return;
		}
		for (Long code : allCodes) {
			DiagnosticStudentClassKnowpointForm form = new DiagnosticStudentClassKnowpointForm();
			form.setClassId(classId);
			form.setStudentId(studentId);
			form.setKnowpointCode(code);
			form.setDoCount(doCountMap.get(code));
			form.setMinDifficulty(BigDecimal.valueOf(minDiffMap.get(code)));
			form.setMaxDifficulty(BigDecimal.valueOf(maxDiffMap.get(code)));
			form.setRightCount(rightCountMap.get(code) == null ? 0 : rightCountMap.get(code));
			// 冲刺题
			form.setDoHard3Count(doHard3CountMap.get(code) == null ? 0 : doHard3CountMap.get(code));
			form.setRightHard3Count(rightHard3CountMap.get(code) == null ? 0 : rightHard3CountMap.get(code));
			// 提高题
			form.setDoHard2Count(doHard2CountMap.get(code) == null ? 0 : doHard2CountMap.get(code));
			form.setRightHard2Count(rightHard2CountMap.get(code) == null ? 0 : rightHard2CountMap.get(code));
			// 基础题
			form.setDoHard1Count(doHard1CountMap.get(code) == null ? 0 : doHard1CountMap.get(code));
			form.setRightHard1Count(rightHard1CountMap.get(code) == null ? 0 : rightHard1CountMap.get(code));
			this.saveStuClassKp(form);
		}
	}

	@Transactional
	public void saveStuClassKp(DiagnosticStudentClassKnowpointForm form) {
		Params params = Params.param("classId", form.getClassId());
		params.put("studentId", form.getStudentId());
		params.put("knowpointCode", form.getKnowpointCode());
		DiagnosticStudentClassKnowpoint point = diagnosticStudentClassKnowpointRepo.find("$taskGetStuClassKp", params)
				.get();
		if (point == null) {
			point = new DiagnosticStudentClassKnowpoint();
			point.setKnowpointCode(form.getKnowpointCode());
			point.setStudentId(form.getStudentId());
			point.setClassId(form.getClassId());
			point.setCreateAt(new Date());
			point.setDoCount(form.getDoCount());
			point.setRightCount(form.getRightCount());
			point.setMinDifficulty(form.getMinDifficulty());
			point.setMaxDifficulty(form.getMaxDifficulty());
		} else {
			point.setUpdateAt(new Date());
			point.setRightCount(form.getRightCount() + point.getRightCount());
			point.setDoCount(form.getDoCount() + point.getDoCount());
			if (form.getMinDifficulty().compareTo(point.getMinDifficulty()) == -1) {
				point.setMinDifficulty(form.getMinDifficulty());
			}
			if (form.getMaxDifficulty().compareTo(point.getMaxDifficulty()) == 1) {
				point.setMaxDifficulty(form.getMaxDifficulty());
			}
		}
		point.setDoHard1Count(point.getDoHard1Count() + form.getDoHard1Count());
		point.setDoHard2Count(point.getDoHard2Count() + form.getDoHard2Count());
		point.setDoHard3Count(point.getDoHard3Count() + form.getDoHard3Count());
		point.setRightHard1Count(point.getRightHard1Count() + form.getRightHard1Count());
		point.setRightHard2Count(point.getRightHard2Count() + form.getRightHard2Count());
		point.setRightHard3Count(point.getRightHard3Count() + form.getRightHard3Count());
		if (point.getDoCount() > 0) {
			point.setRightRate(new BigDecimal(point.getRightCount() * 100d / point.getDoCount()).setScale(2,
					BigDecimal.ROUND_HALF_UP));
		} else {
			point.setRightRate(BigDecimal.valueOf(0));
		}
		diagnosticStudentClassKnowpointRepo.save(point);
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

	@Override
	public CursorPage<Long, Student> getAllStudent(CursorPageable<Long> cursorPageable) {
		return studentRepo.find("$taskGetAllByPage").fetch(cursorPageable);
	}

	/**
	 * 第一次，查询出全部的数据，后面只需要查最近24小时，已下发数据
	 * 
	 * @param startTime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> queryLatestList(Date startTime, Date endTime, Long clazzId) {
		Params params = Params.param("clazzId", clazzId);
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}
		return stuHomeworkRepo.find("$queryLatestList", params).list(Map.class);
	}

	/**
	 * 查询普通作业列表
	 * 
	 * @param studentId
	 * @param classId
	 * @param studentId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> queryCommonHkList(Long studentId, Long classId) {
		Params params = Params.param("studentId", studentId).put("classId", classId);
		return stuHomeworkRepo.find("$queryCommonHkList", params).list(Map.class);
	}

	/**
	 * 查询普通和寒假作业列表
	 * 
	 * @param holidayHomeworkId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> queryHolidayHkList(Long studentId, Long classId, Long hkId) {
		Params params = Params.param("studentId", studentId).put("classId", classId);
		return stuHomeworkRepo.find("$queryHolidayHkList", params).list(Map.class);
	}

	public DiagnosticStudentClassLatestHomework getDiagnosticStuClassLatestHk(Long studentId, Long homeworkId) {
		return diagnosticStuClassLatestHkRepo.find("$getDiagnosticStuClassLatestHk",
				Params.param("studentId", studentId).put("homeworkId", homeworkId)).get();
	}

	public List<DiagnosticStudentClassLatestHomework> getDiagnosticStuClassLatestHk(Long studentId) {
		return diagnosticStuClassLatestHkRepo
				.find("$getDiagnosticStuClassLatestHk", Params.param("studentId", studentId)).list();
	}

	public void deleteDiagnosticStuClassLatestHk(Date startTime_early, Long studentId) {
		diagnosticStuClassLatestHkRepo.execute("$deleteDiagnosticStuClassLatestHk",
				Params.param("startTime", startTime_early).put("studentId", studentId));
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
	 * web端新增7,15次查询，相应知识点统计增加
	 * 
	 * @param stuHkIds
	 * @param studentId
	 * @param clazzId
	 */
	public void dealCommonStuClassLastestHkpt(List<Long> stuHkIds, Long studentId, Long clazzId) {
		List<StudentHomeworkQuestion> list = studentHomeworkQuestionRepo
				.find("$findQuestions", Params.param("stuHkIds", stuHkIds)).list();
		List<Long> stuHkIds_15 = new ArrayList<Long>();
		List<Long> stuHkIds_7 = new ArrayList<Long>();
		stuHkIds_15 = stuHkIds.size() >= 15 ? stuHkIds.subList(0, 15) : stuHkIds;
		stuHkIds_7 = stuHkIds.size() >= 7 ? stuHkIds.subList(0, 7) : stuHkIds;
		for (StudentHomeworkQuestion stuq : list) {
			this.lastToSave(clazzId, studentId, stuq.getQuestionId(), stuq.getResult(), 30);
			if (stuHkIds_15.contains(stuq.getStudentHomeworkId())) {
				this.lastToSave(clazzId, studentId, stuq.getQuestionId(), stuq.getResult(), 15);
			}
			if (stuHkIds_7.contains(stuq.getStudentHomeworkId())) {
				this.lastToSave(clazzId, studentId, stuq.getQuestionId(), stuq.getResult(), 7);
			}
		}

	}

	public DiagnosticStudentClassLatestHomeworkKnowpoint find(Long classId, Long studentId, Long knowpointCode,
			int times) {
		return diagnosticStudentClassLatestHomeworkKnowpointRepo
				.find("$taskGetStuClassLatestHkKp", Params.param("studentId", studentId)
						.put("knowpointCode", knowpointCode).put("classId", classId).put("times", times))
				.get();
	}

	@Override
	public List<Long> queryClazzIds(Long studentId) {
		return homeworkStudentClazzRepo.find("$queryClazzIds", Params.param("studentId", studentId)).list(Long.class);
	}

	@Override
	public Long count() {
		Long count = diagnosticStudentClassKnowpointRepo.find("getStuClassKpCount").get(Long.class);
		return count;
	}

	@Override
	public Long count2() {
		Long count = diagnosticStudentClassLatestHomeworkKnowpointRepo.find("$getStuLastestClassKpCount")
				.get(Long.class);
		return count;
	}

}
