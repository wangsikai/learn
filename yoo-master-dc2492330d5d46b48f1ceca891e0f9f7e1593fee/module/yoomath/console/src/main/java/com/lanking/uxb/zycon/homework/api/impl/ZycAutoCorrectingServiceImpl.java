package com.lanking.uxb.zycon.homework.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.constants.MqIntelligentCorrectionRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.uxb.service.question.api.AnswerService;
import com.lanking.uxb.zycon.homework.api.ZycAutoCorrectingService;
import com.lanking.uxb.zycon.homework.api.ZycStudentHomeworkAnswerService;
import com.lanking.uxb.zycon.homework.api.ZycStudentHomeworkQuestionService;

@Service
@Transactional(readOnly = true)
public class ZycAutoCorrectingServiceImpl implements ZycAutoCorrectingService {
	@Autowired
	private ZycStudentHomeworkQuestionService shqService;
	@Autowired
	private ZycStudentHomeworkAnswerService shaService;
	@Autowired
	private AnswerService answerService;
	@Autowired
	private MqSender mqSender;

	@Override
	@Transactional
	@Async
	public void asyncAutoCheck(long stuHkId, long stuHkQId, HomeworkAnswerResult result) {
		StudentHomeworkQuestion stuHkQ = shqService.get(stuHkQId);
		List<StudentHomeworkAnswer> stuAnswers = shaService.find(stuHkQId);
		// 归档答案
		List<Answer> answers = answerService.getQuestionAnswers(stuHkQ.getQuestionId());

		int i = 0;
		for (Answer answer : answers) {
			StudentHomeworkAnswer stuAnswer = stuAnswers.get(i);
			JSONObject jo = new JSONObject();
			jo.put("answerId", answer.getId());
			jo.put("content", stuAnswer.getContent());
			jo.put("result", stuAnswer.getResult());
			mqSender.send(MqIntelligentCorrectionRegistryConstants.EX_INTELLIGENTCORRECTION,
					MqIntelligentCorrectionRegistryConstants.RK_INTELLIGENTCORRECTION_ARCHIVE,
					MQ.builder().data(jo).build());
			i++;
		}
	}
}
