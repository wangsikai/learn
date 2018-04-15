package com.lanking.uxb.service.diagnostic.api.student.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassKnowpoint;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.diagnostic.api.student.StaticStuDiagnosticClassKpService;
import com.lanking.uxb.service.diagnostic.form.DiagnosticStudentClassKnowpointForm;

@Transactional(readOnly = true)
@Service
public class StaticStuDiagnosticClassKpServiceImpl implements StaticStuDiagnosticClassKpService {

	@Autowired
	@Qualifier("DiagnosticStudentClassKnowpointRepo")
	private Repo<DiagnosticStudentClassKnowpoint, Long> diagnosticStudentClassKnowpointRepo;

	@Autowired
	@Qualifier("StudentHomeworkRepo")
	private Repo<StudentHomework, Long> stuHomeworkRepo;

	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	private Repo<HomeworkStudentClazz, Long> homeworkStudentClazzRepo;

	@Autowired
	@Qualifier("QuestionRepo")
	private Repo<Question, Long> questionRepo;

	@Autowired
	@Qualifier("StudentRepo")
	private Repo<Student, Long> studentRepo;

	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private KnowledgeSectionService knowledgeSectionService;
	@Autowired
	private StaticStuDiagnosticClassKpService stuDiagKpService;

	private static final int FETCH_SIZE = 100;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public void statStuClassKp(Date startTime, Date endTime, Long studentId) {
		CursorPageable<Long> cursorPageable = CP.cursor(Long.MAX_VALUE, FETCH_SIZE);
		CursorPage<Long, Map> cursorPage = this.findStuHkList(cursorPageable, studentId, startTime, endTime);
		while (CollectionUtils.isNotEmpty(cursorPage.getItems())) {
			List<Long> questionIds = new ArrayList<Long>();
			List<HomeworkAnswerResult> questionResultList = new ArrayList<HomeworkAnswerResult>();
			if (CollectionUtils.isNotEmpty(cursorPage.getItems())) {
				for (Map stuHk : cursorPage.getItems()) {
					Long questionId = Long.parseLong(stuHk.get("question_id").toString());
					Integer result = Integer.parseInt(stuHk.get("result").toString());
					questionIds.add(questionId);
					questionResultList.add(HomeworkAnswerResult.findByValue(result));
				}
				this.formToSave(studentId, questionIds, questionResultList);
			}
			Long nextCursor = cursorPage.getNextCursor();
			cursorPageable = CP.cursor(nextCursor, FETCH_SIZE);
			cursorPage = this.findStuHkList(cursorPageable, studentId, startTime, endTime);
		}
	}

	@Transactional
	@Override
	public void statWhenLeave(long studentId, long classId) {
		this.deleteByStuId(studentId, classId);
	}

	@Transactional
	@Override
	public void statWhenJoin(long studentId, long classId) {
		// 查询非传进来classId的classId集合,因为触发这个之前，学生已经加入到这个班级了
		List<Long> classIds = stuDiagKpService.findClassIds(studentId, classId);
		if (CollectionUtils.isNotEmpty(classIds)) {
			// 如果不为空，把其他班级的数据复制一份
			Params params = Params.param("classId", classIds.get(0)).put("studentId", studentId);
			List<DiagnosticStudentClassKnowpoint> points = diagnosticStudentClassKnowpointRepo.find(
					"$taskGetStuClassKp", params).list();
			for (DiagnosticStudentClassKnowpoint point : points) {
				DiagnosticStudentClassKnowpoint temp = new DiagnosticStudentClassKnowpoint();
				temp.setClassId(classId);
				temp.setCreateAt(point.getCreateAt());
				temp.setDoCount(point.getDoCount());
				temp.setDoHard1Count(point.getDoHard1Count());
				temp.setDoHard2Count(point.getDoHard2Count());
				temp.setDoHard3Count(point.getDoHard3Count());
				temp.setKnowpointCode(point.getKnowpointCode());
				temp.setMaxDifficulty(point.getMaxDifficulty());
				temp.setMinDifficulty(point.getMinDifficulty());
				temp.setRightCount(point.getRightCount());
				temp.setRightHard1Count(point.getRightHard1Count());
				temp.setRightHard2Count(point.getRightHard2Count());
				temp.setRightHard3Count(point.getRightHard3Count());
				temp.setRightRate(point.getRightRate());
				temp.setStudentId(studentId);
				diagnosticStudentClassKnowpointRepo.save(temp);
			}
		} else {
			this.deleteByStuId(studentId, classId);
			// // 如果为空，需要重新计算一下,到今天0点之前的
			// Calendar calendar = Calendar.getInstance();
			// calendar.setTime(new Date());
			// calendar.set(Calendar.HOUR_OF_DAY, 0);
			// calendar.set(Calendar.MINUTE, 0);
			// calendar.set(Calendar.SECOND, 0);
			// Date endTime = calendar.getTime();
			this.statStuClassKp(null, new Date(), studentId);
		}
	}

	@Transactional
	@Override
	public void deleteByStuId(long studentId, long classId) {
		diagnosticStudentClassKnowpointRepo.execute("$deleteByStuId",
				Params.param("studentId", studentId).put("classId", classId));
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
	@Transactional
	public void formToSave(Long studentId, List<Long> questionIds, List<HomeworkAnswerResult> questionResultList) {
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
			List<Long> classIds = this.findClassIds(studentId, null);
			for (Long classId : classIds) {
				form.setClassId(classId);
				this.saveStuClassKp(form);
			}

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

	@SuppressWarnings("rawtypes")
	@Override
	public CursorPage<Long, Map> findStuHkList(CursorPageable<Long> pageable, long studentId, Date startTime,
			Date endTime) {
		Params params = Params.param("studentId", studentId);
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}

		return stuHomeworkRepo.find("$findStuHkList", params).fetch(pageable, Map.class, new CursorGetter<Long, Map>() {
			@Override
			public Long getCursor(Map bean) {
				return Long.parseLong(String.valueOf(bean.get("id")));
			}
		});
	}

	@Override
	public List<Long> findClassIds(long studentId, Long classId) {
		Params params = Params.param("studentId", studentId);
		if (classId != null) {
			params.put("classId", classId);
		}
		return homeworkStudentClazzRepo.find("$queryClazzIds", params).list(Long.class);
	}

	@Override
	public CursorPage<Long, Long> getAllStudent(CursorPageable<Long> cursorPageable) {
		return studentRepo.find("$taskGetAllByPage").fetch(cursorPageable, Long.class);
	}

	@Transactional
	@SuppressWarnings("rawtypes")
	@Override
	public void statStuClassKp(long classId, Long hkId) {
		List<Map> stuHkList = stuHomeworkRepo.find("$findStuHkListByHomework", Params.param("hkId", hkId)).list(
				Map.class);
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
			this.formToSave(studentId, stuQuesMap.get(studentId), stuResultMap.get(studentId));
		}
	}
}
