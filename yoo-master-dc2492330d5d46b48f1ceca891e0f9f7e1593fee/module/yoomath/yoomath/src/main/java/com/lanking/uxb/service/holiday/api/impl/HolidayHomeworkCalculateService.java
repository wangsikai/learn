package com.lanking.uxb.service.holiday.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.mq.common.constants.MqYoomathFallibleRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItem;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemAnswer;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemQuestionService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.zuoye.api.ZyTeacherFallibleQuestionService;

/**
 * 计算假期作业相关服务
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
@Service
@Transactional(readOnly = true)
public class HolidayHomeworkCalculateService {
	@Autowired
	@Qualifier("HolidayHomeworkRepo")
	private Repo<HolidayHomework, Long> holidayHomeworkRepo;
	@Autowired
	@Qualifier("HolidayHomeworkItemRepo")
	private Repo<HolidayHomeworkItem, Long> holidayHomeworkItemRepo;
	@Autowired
	@Qualifier("HolidayStuHomeworkRepo")
	private Repo<HolidayStuHomework, Long> holidayStuHomeworkRepo;
	@Autowired
	@Qualifier("HolidayStuHomeworkItemRepo")
	private Repo<HolidayStuHomeworkItem, Long> holidayStuHomeworkItemRepo;
	@Autowired
	@Qualifier("HolidayStuHomeworkItemQuestionRepo")
	private Repo<HolidayStuHomeworkItemQuestion, Long> holidayStuHomeworkItemQuestionRepo;
	@Autowired
	@Qualifier("HolidayStuHomeworkItemAnswerRepo")
	private Repo<HolidayStuHomeworkItemAnswer, Long> holidayStuHomeworkItemAnswerRepo;

	@Autowired
	private HolidayHomeworkItemQuestionService holidayHomeworkItemQuestionService;
	@Autowired
	private ZyTeacherFallibleQuestionService teacherFallibleQuestionService;
	@Autowired
	private StudentQuestionAnswerService studentQuestionAnswerService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private UserService userService;
	@Autowired
	private MqSender mqSender;

	/**
	 * 计算假期作业专项正确率
	 *
	 * @param items
	 *            专项列表
	 */
	@Transactional
	public Map<String, Collection<Long>> calculateStuItems(List<HolidayStuHomeworkItem> items) {
		Set<Long> hkIds = Sets.newHashSet();
		Map<String, Collection<Long>> retMap = new HashMap<String, Collection<Long>>(2);
		for (HolidayStuHomeworkItem item : items) {
			List<HolidayStuHomeworkItemQuestion> stuItemQuestions = holidayStuHomeworkItemQuestionRepo
					.find("$queryByItem", Params.param("itemId", item.getId()))
					.list(HolidayStuHomeworkItemQuestion.class);

			int rightCount = 0, wrongCount = 0;
			for (HolidayStuHomeworkItemQuestion q : stuItemQuestions) {
				if (!(q.isAutoCorrect() && q.isManualCorrect())) {
					rightCount = 0;
					wrongCount = 0;
					break;
				} else {
					if (HomeworkAnswerResult.WRONG == q.getResult()) {
						wrongCount++;
					} else if (HomeworkAnswerResult.RIGHT == q.getResult()) {
						rightCount++;
					} else {
						rightCount = 0;
						wrongCount = 0;
						break;
					}
				}
			}

			if (stuItemQuestions.size() > 0 && (rightCount + wrongCount) == stuItemQuestions.size()) {
				item = holidayStuHomeworkItemRepo.get(item.getId());

				item.setRightCount(rightCount);
				item.setWrongCount(wrongCount);

				Double rightRate = (rightCount * 100d) / (rightCount + wrongCount);
				item.setRightRate(BigDecimal.valueOf(rightRate).setScale(0, BigDecimal.ROUND_HALF_UP));
				if (item.getStatus() != StudentHomeworkStatus.ISSUED) {
					item.setStatus(StudentHomeworkStatus.SUBMITED);
				}
				item.setCompletionRate(new BigDecimal(100));

				holidayStuHomeworkItemRepo.save(item);

				// 统计学生作业正确率
				calculateStuHomework(item.getHolidayStuHomeworkId());
				// 统计假期作业专项正确率
				calculateHolidayHomeworkItem(item.getHolidayHomeworkItemId());
				// 统计假期作业正确率
				calculateHolidayHomework(item.getHolidayHomeworkId());

				hkIds.add(item.getHolidayHomeworkId());

				HolidayHomework homework = holidayHomeworkRepo.get(item.getHolidayHomeworkId());

				// 更新专项中的题目正确率
				for (HolidayStuHomeworkItemQuestion q : stuItemQuestions) {
					if (HomeworkAnswerResult.WRONG == q.getResult()) {
						holidayHomeworkItemQuestionService.updateStatistics(q.getQuestionId(),
								q.getHolidayHomeworkItemId(), 0, 1);
						Question questionVal = questionService.get(q.getQuestionId());

						mqSender.send(MqYoomathFallibleRegistryConstants.EX_YM_FALLIBLE_TASK,
								MqYoomathFallibleRegistryConstants.RK_YM_FALLIBLE_TASK_TEACHER,
								MQ.builder().data(new JSONObject(ValueMap.value("teacherId", homework.getCreateId())
										.put("questionId", questionVal.getId()).put("result", q.getResult())
										.put("subjectCode", questionVal.getSubjectCode())
										.put("type", questionVal.getType()).put("typeCode", questionVal.getTypeCode())
										.put("difficulty", questionVal.getDifficulty()))).build());
					} else if (HomeworkAnswerResult.RIGHT == q.getResult()) {
						holidayHomeworkItemQuestionService.updateStatistics(q.getQuestionId(),
								q.getHolidayHomeworkItemId(), 1, 0);
					}

					Map<Long, List<String>> latexAnswers = Maps.newHashMap();
					Map<Long, List<String>> aciiAnswers = Maps.newHashMap();
					List<HolidayStuHomeworkItemAnswer> answers = holidayStuHomeworkItemAnswerRepo
							.find("$queryItemAnswers", Params.param("itemQuestionId", q.getId())).list();
					List<String> lAnswers = new ArrayList<String>(answers.size());
					List<String> aAnswers = new ArrayList<String>(answers.size());
					Date answerAt = null;
					for (HolidayStuHomeworkItemAnswer a : answers) {
						answerAt = a.getAnswerAt();
						lAnswers.add(a.getContent());
						aAnswers.add(a.getContentAscii());
					}

					latexAnswers.put(q.getQuestionId(), lAnswers);
					aciiAnswers.put(q.getQuestionId(), aAnswers);
					List<Long> answerImgs = null;
					if (q.getAnswerImg() != null && q.getAnswerImg() > 0) {
						answerImgs = new ArrayList<Long>(1);
						answerImgs.add(q.getAnswerImg());
					}
					studentQuestionAnswerService.create(q.getStudentId(), q.getQuestionId(), latexAnswers, aciiAnswers,
							answerImgs, null, q.getRightRate(), q.getResult(),
							StudentQuestionAnswerSource.HOLIDAY_HOMEWORK, answerAt == null ? new Date() : answerAt);

				}
			}
		}

		retMap.put("hkIds", hkIds);
		return retMap;
	}

	/**
	 * 计算假期作业学生作业正确率
	 *
	 * @param stuHkId
	 *            学生假期作业id
	 */
	@Transactional
	public void calculateStuHomework(long stuHkId) {
		List<HolidayStuHomeworkItem> stuItems = holidayStuHomeworkItemRepo
				.find("$findItemByStuHk", Params.param("stuHkId", stuHkId)).list();
		int rightCount = 0, wrongCount = 0, completeCount = 0, homeworkTime = 0;
		double rightRate = 0, completeRate = 0;
		boolean allCalculate = true;
		for (HolidayStuHomeworkItem shkItem : stuItems) {
			if (shkItem.getRightRate() == null) {
				allCalculate = false;
				continue;
			}
			rightCount += shkItem.getRightCount();
			wrongCount += shkItem.getWrongCount();
			homeworkTime += shkItem.getHomeworkTime();
			completeCount++;
		}

		rightRate = (rightCount * 100d) / (rightCount + wrongCount);
		homeworkTime = homeworkTime / completeCount;

		HolidayStuHomework holidayStuHomework = holidayStuHomeworkRepo.get(stuHkId);
		holidayStuHomework.setRightCount(rightCount);
		holidayStuHomework.setWrongCount(wrongCount);
		holidayStuHomework.setRightRate(BigDecimal.valueOf(rightRate).setScale(0, BigDecimal.ROUND_HALF_UP));
		holidayStuHomework.setHomeworkTime(homeworkTime);

		// 若此学生作业下的专项全部统计完成更改状态为ISSUED
		// 宋娟说不要，现在注释掉计算学生假期作业完成情况
		/*
		 * completeRate = (completeCount * 100d) / stuItems.size();
		 * holidayStuHomework
		 * .setCompletionRate(BigDecimal.valueOf(completeRate).setScale(0,
		 * BigDecimal.ROUND_HALF_UP));
		 */
		if (allCalculate) {
			holidayStuHomework.setStatus(StudentHomeworkStatus.ISSUED);
		}

		holidayStuHomeworkRepo.save(holidayStuHomework);

	}

	/**
	 * 统计假期作业专项id
	 *
	 * @param hkItemId
	 *            假期专项id
	 */
	@Transactional
	public void calculateHolidayHomeworkItem(long hkItemId) {
		HolidayHomeworkItem hkItem = holidayHomeworkItemRepo.get(hkItemId);

		List<HolidayStuHomeworkItem> stuHkItems = holidayStuHomeworkItemRepo
				.find("$findByHkItem", Params.param("hkItemId", hkItem)).list();
		int rightCount = 0, wrongCount = 0, completeCount = 0, homeworkTime = 0;
		double rightRate = 0, completeRate = 0;
		boolean allCalculate = true;
		for (HolidayStuHomeworkItem item : stuHkItems) {
			if (null == item.getRightRate()) {
				allCalculate = false;
				continue;
			}
			rightCount += item.getRightCount();
			wrongCount += item.getWrongCount();
			homeworkTime += item.getHomeworkTime();
			completeCount++;
		}

		rightRate = (rightCount * 100d) / (rightCount + wrongCount);
		homeworkTime = homeworkTime / completeCount;
		hkItem.setRightCount(rightCount);
		hkItem.setWrongCount(wrongCount);
		hkItem.setRightRate(BigDecimal.valueOf(rightRate).setScale(0, BigDecimal.ROUND_HALF_UP));
		hkItem.setHomeworkTime(homeworkTime);

		completeRate = (completeCount * 100d) / stuHkItems.size();
		hkItem.setCompletionRate(BigDecimal.valueOf(completeRate).setScale(0, BigDecimal.ROUND_HALF_UP));
		// 若此假期作业专项学生全部统计完成，更新状态为NOT_ISSUE
		if (allCalculate) {
			hkItem.setStatus(HomeworkStatus.NOT_ISSUE);
		}

		holidayHomeworkItemRepo.save(hkItem);
	}

	/**
	 * 统计假期作业正确率
	 *
	 * @param hkId
	 *            假期作业id
	 */
	@Transactional
	public void calculateHolidayHomework(long hkId) {
		HolidayHomework homework = holidayHomeworkRepo.get(hkId);

		List<HolidayHomeworkItem> hkItems = holidayHomeworkItemRepo.find("$findByHk", Params.param("hkId", hkId))
				.list();
		int rightCount = 0, wrongCount = 0, completeCount = 0, homeworkTime = 0, distributeCount = 0;
		double rightRate = 0, completeRate = 0;
		boolean allCalculate = true;
		for (HolidayHomeworkItem item : hkItems) {
			distributeCount += item.getDistributeCount();
			if (item.getRightRate() == null) {
				allCalculate = false;
				continue;
			} else if (item.getStatus() != HomeworkStatus.NOT_ISSUE) {
				allCalculate = false;
			}

			rightCount = item.getRightCount();
			wrongCount = item.getWrongCount();
			homeworkTime += item.getHomeworkTime();
			completeCount += item.getCommitCount();
		}

		rightRate = (rightCount * 100d) / (rightCount + wrongCount);
		if (completeCount == 0) {
			homeworkTime = 0;
		} else {
			homeworkTime = homeworkTime / completeCount;
		}
		homework.setRightCount(rightCount);
		homework.setWrongCount(wrongCount);
		homework.setRightRate(BigDecimal.valueOf(rightRate).setScale(0, BigDecimal.ROUND_HALF_UP));
		homework.setHomeworkTime(homeworkTime);

		// 若此假期作业全部完成更新状态为NOT_ISSUE
		completeRate = (completeCount * 100d) / distributeCount;
		homework.setCompletionRate(BigDecimal.valueOf(completeRate).setScale(0, BigDecimal.ROUND_HALF_UP));
		if (allCalculate) {
			homework.setStatus(HomeworkStatus.NOT_ISSUE);
		}

		holidayHomeworkRepo.save(homework);
	}

}
