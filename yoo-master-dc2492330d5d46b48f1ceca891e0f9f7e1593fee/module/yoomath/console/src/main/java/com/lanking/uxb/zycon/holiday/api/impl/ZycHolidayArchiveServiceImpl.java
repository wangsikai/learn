package com.lanking.uxb.zycon.holiday.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.constants.MqIntelligentCorrectionRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemAnswer;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;
import com.lanking.uxb.service.question.api.AnswerService;
import com.lanking.uxb.zycon.holiday.api.ZycHolidayArchiveService;
import com.lanking.uxb.zycon.holiday.api.ZycHolidayStuHomeworkItemAnswerService;
import com.lanking.uxb.zycon.holiday.api.ZycHolidayStuHomeworkItemQuestionService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
@Service
@Transactional(readOnly = true)
public class ZycHolidayArchiveServiceImpl implements ZycHolidayArchiveService {
	@Autowired
	private ZycHolidayStuHomeworkItemAnswerService stuHomeworkItemAnswerService;
	@Autowired
	private ZycHolidayStuHomeworkItemQuestionService stuHomeworkItemQuestionService;
	@Autowired
	private AnswerService answerService;
	@Autowired
	private MqSender mqSender;

	@Override
	@Transactional(readOnly = false)
	public void asyncAnswerArchive(long itemQuestionId, HomeworkAnswerResult result) {
		HolidayStuHomeworkItemQuestion itemQuestion = stuHomeworkItemQuestionService.get(itemQuestionId);
		List<HolidayStuHomeworkItemAnswer> stuAnswers = stuHomeworkItemAnswerService.query(itemQuestionId);

		List<Answer> answers = answerService.getQuestionAnswers(itemQuestion.getQuestionId());

		int i = 0;
		for (Answer answer : answers) {
			HolidayStuHomeworkItemAnswer stuAnswer = stuAnswers.get(i);
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
