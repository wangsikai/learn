package com.lanking.uxb.service.correct.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.mq.common.constants.MqYoomathFallibleRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.service.correct.api.CorrectStatService;
import com.lanking.uxb.service.correct.api.CorrectStudentHomeworkQuestionService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatistic2Service;

/**
 * 批改触发的统计相关数据处理接口实现.
 * 
 * @author wanlong.che
 *
 */
@Service
public class CorrectStatServiceImpl implements CorrectStatService {

	@Autowired
	private QuestionService questionService;
	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private StudentHomeworkService studentHomeworkService;
	@Autowired
	private CorrectStudentHomeworkQuestionService correctStudentHomeworkQuestionService;
	@Autowired
	private ZyHomeworkStatistic2Service homeworkStatistic2Service;

	@Autowired
	@Qualifier("executor")
	private Executor executor;

	@Autowired
	MqSender mqSender;

	@Override
	public void aysncFallibleQuestionHandler(Collection<Long> studentHomeworkQuestionIds) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				fallibleQuestionHandlerThread(studentHomeworkQuestionIds);
			}
		});
	}

	/**
	 * 错题处理.
	 *
	 */
	private void fallibleQuestionHandlerThread(Collection<Long> studentHomeworkQuestionIds) {
		List<StudentHomeworkQuestion> studentHomeworkQuestions = correctStudentHomeworkQuestionService
				.mgetList(studentHomeworkQuestionIds);

		// 过滤错题
		Set<Long> studentHomeworkIds = Sets.newHashSet();
		Set<Long> questionIds = Sets.newHashSet();
		List<StudentHomeworkQuestion> fallibleQuestions = Lists.newArrayList();
		for (StudentHomeworkQuestion shq : studentHomeworkQuestions) {
			if (shq.isCorrect() || shq.isNewCorrect() || shq.getResult() == null
					|| shq.getResult() != HomeworkAnswerResult.WRONG) {
				// 排除订正题、非错题
				continue;
			}
			fallibleQuestions.add(shq);
			questionIds.add(shq.getQuestionId());
			studentHomeworkIds.add(shq.getStudentHomeworkId());
		}

		if (studentHomeworkIds.size() > 0) {
			Map<Long, Question> questionMap = questionService.mget(questionIds);
			Map<Long, StudentHomework> studentHomeworkMap = studentHomeworkService.mgetMap(studentHomeworkIds);
			Set<Long> homeworkIds = Sets.newHashSet();
			for (StudentHomework sh : studentHomeworkMap.values()) {
				homeworkIds.add(sh.getHomeworkId());
			}
			Map<Long, Homework> homeworkMap = homeworkService.mget(homeworkIds);

			for (StudentHomeworkQuestion shq : fallibleQuestions) {
				StudentHomework sh = studentHomeworkMap.get(shq.getStudentHomeworkId());
				if (sh.getSubmitAt() == null || sh.getStuSubmitAt() == null) {
					// 只统计提交(自动提交|有效时间内的主动提交)的作业
					continue;
				}

				// 处理记录历史答题+处理学生错题
				homeworkStatistic2Service.recordAnswer(sh.getStudentId(), shq);

				// 更新老师错题
				Homework homework = homeworkMap.get(sh.getHomeworkId());
				Question question = questionMap.get(shq.getQuestionId());
				mqSender.send(MqYoomathFallibleRegistryConstants.EX_YM_FALLIBLE_TASK,
						MqYoomathFallibleRegistryConstants.RK_YM_FALLIBLE_TASK_TEACHER,
						MQ.builder()
								.data(new JSONObject(ValueMap.value("teacherId", homework.getCreateId())
										.put("questionId", question.getId()).put("result", HomeworkAnswerResult.WRONG)
										.put("subjectCode", question.getSubjectCode()).put("type", question.getType())
										.put("typeCode", question.getTypeCode())
										.put("difficulty", question.getDifficulty())))
								.build());
			}
		}
	}

}
