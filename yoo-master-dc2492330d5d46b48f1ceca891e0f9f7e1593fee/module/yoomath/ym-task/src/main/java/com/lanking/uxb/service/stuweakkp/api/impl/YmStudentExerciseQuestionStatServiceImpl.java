package com.lanking.uxb.service.stuweakkp.api.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.QuestionSection;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.stat.StudentExerciseKnowpoint;
import com.lanking.cloud.domain.yoomath.stat.StudentExerciseSection;
import com.lanking.cloud.domain.yoomath.stat.StudentQuestionAnswer;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.stuweakkp.api.YmStudentExerciseQuestionStatService;

/**
 * @see YmStudentExerciseQuestionStatService
 * @author xinyu.zhou
 * @since 2.6.0
 */
@Service
@Transactional(readOnly = true)
public class YmStudentExerciseQuestionStatServiceImpl implements YmStudentExerciseQuestionStatService {

	@Autowired
	@Qualifier("StudentExerciseSectionRepo")
	private Repo<StudentExerciseSection, Long> sectionExerciseRepo;

	@Autowired
	@Qualifier("StudentExerciseKnowpointRepo")
	private Repo<StudentExerciseKnowpoint, Long> knowpointExerciseRepo;

	@Autowired
	@Qualifier("QuestionSectionRepo")
	private Repo<QuestionSection, Long> questionSectionRepo;

	@Autowired
	@Qualifier("StudentQuestionAnswerRepo")
	private Repo<StudentQuestionAnswer, Long> studentQuestionAnswerRepo;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

	@Override
	@Transactional
	public void updateExerciseQuestion(long studentId, long questionId, HomeworkAnswerResult result, long sqaId) {
		List<QuestionSection> questionSections = questionSectionRepo
				.find("$ymGetByQuestionId", Params.param("questionId", questionId)).list();
		Calendar cal = Calendar.getInstance();
		Long curMonth = Long.valueOf(sdf.format(cal.getTime()));
		// 题目对应的所有章节都进行处理
		for (QuestionSection q : questionSections) {
			Params params = Params.param();
			params.put("questionId", questionId);
			params.put("studentId", studentId);
			params.put("sectionCode", q.getSectionCode());
			params.put("sqaId", sqaId);

			StudentExerciseSection exerciseSection = sectionExerciseRepo.find("$ymGetBySectionCodeAndStudentId", params)
					.get();
			if (null == exerciseSection) {
				exerciseSection = new StudentExerciseSection();
				exerciseSection.setCreateAt(new Date());
				exerciseSection.setDoCount(0);
				exerciseSection.setWrongCount(0);
				exerciseSection.setSectionCode(q.getSectionCode());
				exerciseSection.setStudentId(studentId);
				exerciseSection.setCurMonth(curMonth);
				cal.add(Calendar.MONTH, -1);
				Long lastMonth = Long.valueOf(sdf.format(cal.getTime()));
				exerciseSection.setLastMonth(lastMonth);
			} else if (curMonth == exerciseSection.getCurMonth()) {
				exerciseSection
						.setCurMonthWrongCount(calculateWrongCount(exerciseSection.getWrongCount(), result, params));
				exerciseSection.setCurMonthDoCount(exerciseSection.getCurMonthDoCount() + 1);
				if (exerciseSection.getLastMonth() == 0) {
					cal.add(Calendar.MONTH, -1);
					Long lastMonth = Long.valueOf(sdf.format(cal.getTime()));
					exerciseSection.setLastMonth(lastMonth);
				}
			} else {
				exerciseSection.setLastMonth(exerciseSection.getCurMonth());
				exerciseSection.setLastMonthDoCount(exerciseSection.getCurMonthDoCount());
				exerciseSection.setLastMonthWrongCount(exerciseSection.getCurMonthWrongCount());
				exerciseSection.setCurMonth(curMonth);
				exerciseSection.setCurMonthWrongCount(calculateWrongCount(0, result, params));
				exerciseSection.setCurMonthDoCount(1);
			}
			exerciseSection.setWrongCount(calculateWrongCount(exerciseSection.getWrongCount(), result, params));
			exerciseSection.setDoCount(exerciseSection.getDoCount() + 1);
			exerciseSection.setUpdateAt(new Date());

			sectionExerciseRepo.save(exerciseSection);

		}

	}

	@Override
	@Transactional
	public void updateExerciseQuestionKnowledge(long studentId, long questionId, HomeworkAnswerResult result,
			long sqaId) {
		// 新知识点处理
		Map<Long, List<Long>> questionKnowledgeMap = questionKnowledgeService
				.mgetByQuestions(Lists.newArrayList(questionId));
		for (Map.Entry<Long, List<Long>> entry : questionKnowledgeMap.entrySet()) {
			for (Long knowledgeCode : entry.getValue()) {
				Params params = Params.param();
				params.put("knowledgeCode", knowledgeCode);
				params.put("studentId", studentId);
				params.put("questionId", entry.getKey());
				params.put("sqaId", sqaId);
				StudentExerciseKnowpoint exerciseKnowpoint = knowpointExerciseRepo
						.find("$ymGetByMetaAndStudentId", params).get();
				if (null == exerciseKnowpoint) {
					exerciseKnowpoint = new StudentExerciseKnowpoint();
					exerciseKnowpoint.setCreateAt(new Date());
					exerciseKnowpoint.setDoCount(0);
					exerciseKnowpoint.setWrongCount(0);
					exerciseKnowpoint.setKnowpointCode(knowledgeCode);
					exerciseKnowpoint.setStudentId(studentId);
				}

				exerciseKnowpoint.setWrongCount(calculateWrongCount(exerciseKnowpoint.getWrongCount(), result, params));
				exerciseKnowpoint.setDoCount(exerciseKnowpoint.getDoCount() + 1);
				exerciseKnowpoint.setUpdateAt(new Date());

				knowpointExerciseRepo.save(exerciseKnowpoint);
			}
		}
	}

	@Override
	@Transactional
	public void updateExerciseQuestions(List<Long> studentIds, List<Long> questionIds,
			List<HomeworkAnswerResult> results, List<Long> sqaIds, List<Date> createAt) {
		List<QuestionSection> questionSections = questionSectionRepo
				.find("$ymGetByQuestionIds", Params.param("questionIds", questionIds)).list();
		Map<Long, List<QuestionSection>> questionSectionMap = new HashMap<Long, List<QuestionSection>>(
				questionIds.size());
		Calendar cal = Calendar.getInstance();
		Long curMonth = Long.valueOf(sdf.format(cal.getTime()));

		for (QuestionSection qs : questionSections) {
			List<QuestionSection> sections = questionSectionMap.get(qs.getQuestionId());
			if (CollectionUtils.isEmpty(sections)) {
				sections = Lists.newArrayList();
			}

			sections.add(qs);

			questionSectionMap.put(qs.getQuestionId(), sections);
		}

		int index = 0;
		for (Long studentId : studentIds) {
			long questionId = questionIds.get(index);

			List<QuestionSection> qses = questionSectionMap.get(questionId);
			if (CollectionUtils.isEmpty(qses)) {
				index++;
				continue;
			}

			Date createAtDate = createAt.get(index);
			Long date = createAtDate == null ? null : Long.valueOf(sdf.format(createAtDate));

			List<Long> sectionCodes = new ArrayList<Long>(qses.size());
			for (QuestionSection section : qses) {
				sectionCodes.add(section.getSectionCode());
			}

			List<StudentExerciseSection> studentExerciseSections = sectionExerciseRepo
					.find("$ymGetBySectionCodesAndStudentId",
							Params.param("studentId", studentId).put("sectionCodes", sectionCodes))
					.list();

			Map<Long, StudentExerciseSection> studentExerciseSectionMap = null;
			if (CollectionUtils.isEmpty(studentExerciseSections)) {
				studentExerciseSectionMap = Collections.EMPTY_MAP;
			} else {
				studentExerciseSectionMap = new HashMap<Long, StudentExerciseSection>(studentExerciseSections.size());
				for (StudentExerciseSection s : studentExerciseSections) {
					studentExerciseSectionMap.put(s.getSectionCode(), s);
				}
			}

			for (QuestionSection section : qses) {
				StudentExerciseSection exerciseSection = studentExerciseSectionMap.get(section.getSectionCode());
				HomeworkAnswerResult result = results.get(index);
				Params params = Params.param();
				params.put("questionId", questionId);
				params.put("studentId", studentId);
				params.put("sectionCode", section.getSectionCode());
				params.put("sqaId", sqaIds.get(index));
				if (null == exerciseSection) {
					exerciseSection = new StudentExerciseSection();
					exerciseSection.setCreateAt(new Date());
					exerciseSection.setDoCount(0);
					exerciseSection.setWrongCount(0);
					exerciseSection.setSectionCode(section.getSectionCode());
					exerciseSection.setStudentId(studentId);
					if (date != null) {
						exerciseSection.setCurMonth(date);
						Long lastMonth = date - 1;
						exerciseSection.setLastMonth(lastMonth);
					}
				} else if (curMonth == exerciseSection.getCurMonth()) {
					exerciseSection.setCurMonthWrongCount(
							calculateWrongCount(exerciseSection.getWrongCount(), result, params));
					exerciseSection.setCurMonthDoCount(exerciseSection.getCurMonthDoCount() + 1);
					if (exerciseSection.getLastMonth() == 0) {
						cal.add(Calendar.MONTH, -1);
						Long lastMonth = Long.valueOf(sdf.format(cal.getTime()));
						exerciseSection.setLastMonth(lastMonth);
					}
				} else if (date != null && date == exerciseSection.getCurMonth()) {
					exerciseSection.setCurMonthWrongCount(
							calculateWrongCount(exerciseSection.getWrongCount(), result, params));
					exerciseSection.setCurMonthDoCount(exerciseSection.getCurMonthDoCount() + 1);
					if (exerciseSection.getLastMonth() == 0) {
						Long lastMonth = date - 1;
						exerciseSection.setLastMonth(lastMonth);
					}
				} else if (date != null) {
					exerciseSection.setLastMonth(exerciseSection.getCurMonth());
					exerciseSection.setLastMonthDoCount(exerciseSection.getCurMonthDoCount());
					exerciseSection.setLastMonthWrongCount(exerciseSection.getCurMonthWrongCount());
					exerciseSection.setCurMonth(date);
					exerciseSection.setCurMonthWrongCount(calculateWrongCount(0, result, params));
					exerciseSection.setCurMonthDoCount(1);
				}
				exerciseSection.setWrongCount(calculateWrongCount(exerciseSection.getWrongCount(), result, params));
				exerciseSection.setDoCount(exerciseSection.getDoCount() + 1);
				exerciseSection.setUpdateAt(new Date());

				sectionExerciseRepo.save(exerciseSection);
			}

			index++;

		}

	}

	@Override
	@Transactional
	public void updateExerciseQuestionKnowledges(List<Long> studentIds, List<Long> questionIds,
			List<HomeworkAnswerResult> results, List<Long> sqaIds) {
		Map<Long, List<Long>> questionKnowledgeMap = questionKnowledgeService.mgetByQuestions(questionIds);
		int index = 0;
		for (Map.Entry<Long, List<Long>> entry : questionKnowledgeMap.entrySet()) {
			Long studentId = studentIds.get(index);
			Long sqaId = sqaIds.get(index);
			HomeworkAnswerResult result = results.get(index);
			if (CollectionUtils.isEmpty(entry.getValue())) {
				index++;
				continue;
			}

			List<StudentExerciseKnowpoint> studentExerciseKnowpoints = knowpointExerciseRepo
					.find("$ymGetByKpsAndStudentId",
							Params.param("studentId", studentId).put("knowledgeCodes", entry.getValue()))
					.list();

			Map<Long, StudentExerciseKnowpoint> knowpointMap = new HashMap<Long, StudentExerciseKnowpoint>(
					studentExerciseKnowpoints.size());

			for (StudentExerciseKnowpoint k : studentExerciseKnowpoints) {
				knowpointMap.put(k.getKnowpointCode(), k);
			}
			for (Long knowledgeCode : entry.getValue()) {
				Params params = Params.param();
				params.put("knowledgeCode", knowledgeCode);
				params.put("studentId", studentId);
				params.put("questionId", entry.getKey());
				params.put("sqaId", sqaId);
				StudentExerciseKnowpoint exerciseKnowpoint = knowpointMap.get(knowledgeCode);
				if (null == exerciseKnowpoint) {
					exerciseKnowpoint = new StudentExerciseKnowpoint();
					exerciseKnowpoint.setCreateAt(new Date());
					exerciseKnowpoint.setDoCount(0);
					exerciseKnowpoint.setWrongCount(0);
					exerciseKnowpoint.setKnowpointCode(knowledgeCode);
					exerciseKnowpoint.setStudentId(studentId);
				}

				exerciseKnowpoint.setWrongCount(calculateWrongCount(exerciseKnowpoint.getWrongCount(), result, params));
				exerciseKnowpoint.setDoCount(exerciseKnowpoint.getDoCount() + 1);
				exerciseKnowpoint.setUpdateAt(new Date());

				knowpointExerciseRepo.save(exerciseKnowpoint);
			}

			index++;
		}
	}

	/**
	 * 计算错误次数
	 *
	 * @param wrongCount
	 *            原错误次数
	 * @param result
	 *            本次答题结果
	 * @return 新错误次数
	 */
	private long calculateWrongCount(long wrongCount, HomeworkAnswerResult result, Params params) {
		StudentQuestionAnswer sqAnswer = studentQuestionAnswerRepo.find("$getLatestQuestionAnswer", params).get();
		if (sqAnswer == null) {
			// 上次答案不存在，且本次做错则初始化数据中错误次数为1
			if (result == HomeworkAnswerResult.WRONG) {
				wrongCount++;
			}
		} else if (sqAnswer.getResult() == HomeworkAnswerResult.RIGHT && result == HomeworkAnswerResult.WRONG) {
			// 上次答案为正确，本次答案错误错误次数+1
			wrongCount++;
		} else if (sqAnswer.getResult() == HomeworkAnswerResult.WRONG && result == HomeworkAnswerResult.RIGHT) {
			// 上次答案错误，本次答案正确，错误次数-1
			wrongCount = wrongCount - 1 < 0 ? 0 : --wrongCount;
		}

		return wrongCount;
	}
}
