package com.lanking.uxb.service.zuoye.api.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.MappedSuperclass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.mq.common.constants.MqYoomathFallibleRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkQuestion;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswerImage;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStat;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStat;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.ExerciseService;
import com.lanking.uxb.service.resources.api.HomeworkQuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerImageService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatistic2Service;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkStatService;
import com.lanking.uxb.service.zuoye.api.ZyTeacherFallibleQuestionService;

@MappedSuperclass
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ZyHomeworkStatistic2ServiceImpl implements ZyHomeworkStatistic2Service {

	@Autowired
	@Qualifier("StudentHomeworkRepo")
	Repo<StudentHomework, Long> stuHkRepo;
	@Autowired
	@Qualifier("HomeworkRepo")
	Repo<Homework, Long> hkRepo;
	@Autowired
	@Qualifier("StudentHomeworkStatRepo")
	Repo<StudentHomeworkStat, Long> stuHkStatRepo;
	@Autowired
	@Qualifier("HomeworkStatRepo")
	Repo<HomeworkStat, Long> hkStatRepo;
	@Autowired
	@Qualifier("HomeworkQuestionRepo")
	Repo<HomeworkQuestion, Long> hkQuestionRepo;
	@Autowired
	@Qualifier("StudentHomeworkQuestionRepo")
	Repo<StudentHomeworkQuestion, Long> stuHkQuestionRepo;
	@Autowired
	HomeworkQuestionService hkQuestionService;
	@Autowired
	StudentHomeworkService stuHkService;
	@Autowired
	StudentHomeworkAnswerService stuHkAnswerService;
	@Autowired
	StudentHomeworkQuestionService stuHkQuestionService;
	@Autowired
	ZyStudentHomeworkStatService stuHkStatService;
	@Autowired
	ZyHomeworkStatService hkStatService;
	@Autowired
	ZyStudentFallibleQuestionService stuFallQuestionService;
	@Autowired
	ZyTeacherFallibleQuestionService teaFallQuestionService;
	@Autowired
	StudentQuestionAnswerService stuQuestionAnswerService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	ExerciseService exerciseService;
	@Autowired
	MqSender mqSender;
	@Autowired
	IndexService indexService;
	@Autowired
	HomeworkService hkService;
	@Autowired
	private StudentHomeworkAnswerImageService studentHomeworkAnswerImageService;

	@Transactional(readOnly = false)
	@Override
	public void staticAfterIssue(long homeworkId) {
		this.staticHomeworkAfterIssue(homeworkId);
		this.staticHomeworkQuestionAfterIssue(homeworkId);
	}

	@Transactional(readOnly = false)
	@Override
	public void staticHomeworkAfterIssue(long homeworkId) {
		// 统计作业平均时长
		Params params = Params.param("hkId", homeworkId);
		Map<String, Object> map = stuHkRepo.find("$zyStaticHomeworkTime", Params.param("homeworkId", homeworkId))
				.get(Map.class);
		if (map.get("homework_time") != null) {
			params.put("homeworkTime", ((BigDecimal) map.get("homework_time")).intValue());
		} else {
			params.put("homeworkTime", 0);
		}
		// 统计作业平均正确率
		List<Map> hkStatic = stuHkQuestionRepo.find("$zyStatisticHomework", params).list(Map.class);

		int rightCount = 0;
		int wrongCount = 0;
		int halfWrongCount = 0;

		int totalPercent = 0;
		int totalDenominator = 0;

		for (Map m : hkStatic) {
			int cou = ((BigInteger) m.get("cou")).intValue();
			int result = ((Byte) m.get("result")).intValue();
			Byte type = ((Byte) m.get("type"));
			Short rightRight = ((Short) m.get("right_rate"));

			if (type != null && type.intValue() == Type.QUESTION_ANSWERING.getValue()) {// 简答题
				totalDenominator += 2 * cou;
				if (rightRight != null) {
					int rightRate = rightRight.intValue();
					totalPercent += rightRate * 2 * cou;
					if (rightRate == 100) {
						rightCount += cou;
					} else if (rightRate == 0) {
						wrongCount += cou;
					} else {
						halfWrongCount += cou;
					}
				}
			} else if (type != null && type.intValue() == Type.FILL_BLANK.getValue()) {// 填空题
				totalDenominator += cou;
				if (rightRight == null) {
					if (result == HomeworkAnswerResult.RIGHT.getValue()) {
						rightCount += cou;
						totalPercent += 100 * cou;
					} else {
						wrongCount += cou;
						totalPercent += 0 * cou;
					}
				} else {
					int rightRate = rightRight.intValue();
					totalPercent += rightRate * cou;
					if (rightRate == 100) {
						rightCount += cou;
					} else if (rightRate == 0) {
						wrongCount += cou;
					} else {
						halfWrongCount += cou;
					}
				}
			} else {
				totalDenominator += cou;
				if (result == HomeworkAnswerResult.RIGHT.getValue()) {
					rightCount += cou;
					totalPercent += 100 * cou;
				} else {
					wrongCount += cou;
					totalPercent += 0 * cou;
				}
			}
		}
		params.put("rightCount", rightCount);
		params.put("wrongCount", wrongCount);
		params.put("halfWrongCount", halfWrongCount);

		if (totalPercent == 0) {
			params.put("hkRightRate", 0);
		} else {
			params.put("hkRightRate",
					BigDecimal.valueOf(totalPercent * 1f / totalDenominator).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		if (totalDenominator > 0) {
			// 更新作业统计
			hkRepo.execute("$zyUpdateHomeworkStat", params);
		}

		// 更新整份作业统计正确率标记
		hkRepo.execute("$updateHomeworkRightRateStatFlag",
				Params.param("homeworkId", homeworkId).put("rightRateStatFlag", true));
	}

	@Transactional(readOnly = false)
	@Override
	public void staticHomeworkQuestionAfterIssue(long homeworkId) {
		List<Map> hkQuestionStatic = stuHkQuestionRepo
				.find("$zyStatisticHomeworkQuestion", Params.param("hkId", homeworkId)).list(Map.class);
		List<Map> hkQuestionHalfWrongStatic = stuHkQuestionRepo
				.find("$zyStatisticHomeworkQuestionHalfWrongCount", Params.param("hkId", homeworkId)).list(Map.class);
		List<Map> hkQuestionDoTime = stuHkQuestionRepo
				.find("$zyStatisticQuestionDoTime", Params.param("hkId", homeworkId)).list(Map.class);

		Map<Long, Integer> doTimeMap = new HashMap<Long, Integer>();
		for (Map m : hkQuestionDoTime) {
			long questionId = ((BigInteger) m.get("question_id")).longValue();
			BigDecimal doTimeInteger = (BigDecimal) m.get("do_time");
			int doTime = doTimeInteger == null ? 0 : doTimeInteger.intValue();

			doTimeMap.put(questionId, doTime);
		}
		Map<Long, Map<String, Integer>> couMap = Maps.newHashMap();

		for (Map m : hkQuestionStatic) {
			int cou = ((BigInteger) m.get("cou")).intValue();
			long questionId = ((BigInteger) m.get("question_id")).longValue();
			int result = ((Byte) m.get("result")).intValue();
			Byte type = ((Byte) m.get("type"));
			BigDecimal rightRight = ((BigDecimal) m.get("right_rate"));

			Map<String, Integer> oneMap = couMap.get(questionId);
			if (oneMap == null) {
				oneMap = new HashMap<String, Integer>(6);
				oneMap.put("total", 0);
				oneMap.put("right", 0);
				oneMap.put("halfWrong", 0);
				oneMap.put("rightRight", 0);
			}
			oneMap.put("total", ((Integer) oneMap.get("total")).intValue() + cou);
			if (result == 1) {
				oneMap.put("right", ((Integer) oneMap.get("right")).intValue() + cou);
			}
			if (type != null && type.intValue() == Type.QUESTION_ANSWERING.getValue()) {// 简答题
				oneMap.put("isQuestionAnswering", 1);
				oneMap.put("isFillblank", 0);
				if (rightRight != null) {
					oneMap.put("rightRight", ((Integer) oneMap.get("rightRight")).intValue() + rightRight.intValue());
				}
			} else if (type != null && type.intValue() == Type.FILL_BLANK.getValue()) {// 填空题
				oneMap.put("isQuestionAnswering", 0);
				oneMap.put("isFillblank", 1);
				if (rightRight == null) {
					if (result == 1) {// 正确
						oneMap.put("rightRight", ((Integer) oneMap.get("rightRight")).intValue() + 100);
					} else {
						oneMap.put("rightRight", ((Integer) oneMap.get("rightRight")).intValue() + 0);
					}
				} else {
					oneMap.put("rightRight", ((Integer) oneMap.get("rightRight")).intValue() + rightRight.intValue());
				}
			} else {
				oneMap.put("isQuestionAnswering", 0);
				oneMap.put("isFillblank", 0);
			}
			couMap.put(questionId, oneMap);
		}
		for (Long questionId : couMap.keySet()) {
			Params params = Params.param("hkId", homeworkId);
			params.put("questionId", questionId);
			Map<String, Integer> one = couMap.get(questionId);
			int isQuestionAnswering = one.get("isQuestionAnswering");
			int isFillblank = one.get("isFillblank");
			int right = one.get("right");
			int total = one.get("total");
			params.put("doTime", doTimeMap.get(questionId) == null ? 0 : doTimeMap.get(questionId));
			int halfWrongCount = 0;
			if (isQuestionAnswering == 1) {// 简答题
				for (Map map : hkQuestionHalfWrongStatic) {
					int cou = ((BigInteger) map.get("cou")).intValue();
					long qId = ((BigInteger) map.get("question_id")).longValue();
					if (qId == questionId) {
						halfWrongCount = cou;
						break;
					}
				}
				params.put("halfWrongCount", halfWrongCount);
				params.put("rightCount", right);
				params.put("wrongCount", total - right - halfWrongCount);
				BigDecimal rightRate = BigDecimal.valueOf(one.get("rightRight") * 1f / total).setScale(0,
						BigDecimal.ROUND_HALF_UP);
				params.put("rightRate", rightRate);
			} else if (isFillblank == 1) {// 填空题
				for (Map map : hkQuestionHalfWrongStatic) {
					int cou = ((BigInteger) map.get("cou")).intValue();
					long qId = ((BigInteger) map.get("question_id")).longValue();
					if (qId == questionId) {
						halfWrongCount = cou;
						break;
					}
				}
				params.put("halfWrongCount", halfWrongCount);
				params.put("rightCount", right);
				params.put("wrongCount", total - right - halfWrongCount);
				BigDecimal rightRate = BigDecimal.valueOf(one.get("rightRight") * 1f / total).setScale(0,
						BigDecimal.ROUND_HALF_UP);
				params.put("rightRate", rightRate);
			} else {
				params.put("halfWrongCount", halfWrongCount);
				params.put("rightCount", right);
				params.put("wrongCount", total - right);
				BigDecimal rightRate = BigDecimal.valueOf((right * 100f) / total).setScale(0, BigDecimal.ROUND_HALF_UP);
				params.put("rightRate", rightRate);
			}
			hkQuestionRepo.execute("$zyUpdateHomeworkQuestionStat", params);
		}
	}

	/**
	 * @since 小悠快批，2018-3-6，错题不再通过下发作业job中进行处理，废弃.
	 */
	@Deprecated
	@Transactional(readOnly = false)
	@Override
	public List<Long> staticFall(long homeworkId) {
		List<Long> fallIds = Lists.newArrayList();// 错题记录ID
		Homework homework = hkRepo.get(homeworkId);
		List<HomeworkQuestion> homeworkQuestions = hkQuestionService.getHomeworkQuestion(homeworkId);
		List<Long> homeworkQuestionIds = new ArrayList<Long>(homeworkQuestions.size());
		for (HomeworkQuestion homeworkQuestion : homeworkQuestions) {
			homeworkQuestionIds.add(homeworkQuestion.getQuestionId());
		}
		Map<Long, Question> allQuestionMap = questionService.mget(homeworkQuestionIds);
		List<StudentHomework> shs = stuHkService.listByHomework(homeworkId);
		for (StudentHomework sh : shs) {
			if (sh.getSubmitAt() == null || sh.getStuSubmitAt() == null) {// 只统计提交(自动提交|有效时间内的主动提交)的作业
				continue;
			}
			List<StudentHomeworkQuestion> studentHomeworkQuestions = stuHkQuestionService.find(sh.getId());
			for (StudentHomeworkQuestion studentHomeworkQuestion : studentHomeworkQuestions) {
				if (studentHomeworkQuestion.isCorrect() || studentHomeworkQuestion.isNewCorrect()) {
					// 排除订正题
					continue;
				}
				long qId = studentHomeworkQuestion.getQuestionId();
				if (homeworkQuestionIds.contains(qId)) {
					// 记录历史答案(记录学生答案时发送mq消息统计相关包括记录学生错题)
					recordAnswer(sh.getStudentId(), studentHomeworkQuestion);
					// 更新老师错题
					Question q = allQuestionMap.get(qId);
					mqSender.send(MqYoomathFallibleRegistryConstants.EX_YM_FALLIBLE_TASK,
							MqYoomathFallibleRegistryConstants.RK_YM_FALLIBLE_TASK_TEACHER,
							MQ.builder()
									.data(new JSONObject(ValueMap.value("teacherId", homework.getCreateId())
											.put("questionId", qId).put("result", studentHomeworkQuestion.getResult())
											.put("subjectCode", q.getSubjectCode()).put("type", q.getType())
											.put("typeCode", q.getTypeCode()).put("difficulty", q.getDifficulty())))
									.build());
				}
			}
		}
		return fallIds;
	}

	@Transactional
	@Override
	public void recordAnswer(long studentId, StudentHomeworkQuestion studentHomeworkQuestion) {
		Map<Long, List<String>> answers = Maps.newHashMap();
		Map<Long, List<String>> answerAsciis = Maps.newHashMap();

		List<StudentHomeworkAnswer> studentHomeworkAnswers = stuHkAnswerService.find(studentHomeworkQuestion.getId());
		Date answerAt = null;
		List<String> answerList = Lists.newArrayList();
		List<String> answerAsciiList = Lists.newArrayList();
		List<HomeworkAnswerResult> itemResults = Lists.newArrayList();
		for (StudentHomeworkAnswer studentHomeworkAnswer : studentHomeworkAnswers) {
			answerList.add(studentHomeworkAnswer.getContent() == null ? StringUtils.EMPTY
					: studentHomeworkAnswer.getContent());
			answerAsciiList.add(studentHomeworkAnswer.getContentAscii() == null ? StringUtils.EMPTY
					: studentHomeworkAnswer.getContentAscii());
			if (answerAt == null) {
				answerAt = studentHomeworkAnswer.getAnswerAt();
			}
			if (studentHomeworkQuestion.getType() == Type.FILL_BLANK) {// 如果是填空题的话,保存答案结果列表
				itemResults.add(studentHomeworkAnswer.getResult());
			}
		}
		answers.put(studentHomeworkQuestion.getQuestionId(), answerList);
		answerAsciis.put(studentHomeworkQuestion.getQuestionId(), answerAsciiList);

		List<StudentHomeworkAnswerImage> answerImages = studentHomeworkAnswerImageService
				.findByStuHkQuestion(studentHomeworkQuestion.getId());
		List<Long> answerImgs = new ArrayList<Long>(answerImages.size());
		for (StudentHomeworkAnswerImage studentHomeworkAnswerImage : answerImages) {
			answerImgs.add(studentHomeworkAnswerImage.getNotationAnswerImg() == null
					? studentHomeworkAnswerImage.getAnswerImg() : studentHomeworkAnswerImage.getNotationAnswerImg());
		}
		// 记录历史答案(记录学生答案时发送mq消息统计相关包括记录学生错题)
		// studentHomeworkQuestion
		stuQuestionAnswerService.create(studentId, studentHomeworkQuestion.getQuestionId(), answers, answerAsciis,
				answerImgs, itemResults, studentHomeworkQuestion.getRightRate(), studentHomeworkQuestion.getResult(),
				StudentQuestionAnswerSource.HOMEWORK, answerAt == null ? new Date() : answerAt);
	}
}
