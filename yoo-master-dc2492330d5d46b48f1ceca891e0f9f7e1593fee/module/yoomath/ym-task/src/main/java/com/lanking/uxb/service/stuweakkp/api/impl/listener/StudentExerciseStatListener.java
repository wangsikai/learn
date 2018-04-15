package com.lanking.uxb.service.stuweakkp.api.impl.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDataRegistryConstants;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.uxb.service.stuweakkp.api.YmStudentExerciseQuestionStatService;

@Component
@Exchange(name = MqYoomathDataRegistryConstants.EX_YM_DATA)
public class StudentExerciseStatListener {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private YmStudentExerciseQuestionStatService studentExerciseQuestionStatService;

	@Listener(queue = MqYoomathDataRegistryConstants.QUEUE_YM_DATA_STUDENTEXERCISE, routingKey = MqYoomathDataRegistryConstants.RK_YM_DATA_STUDENTEXERCISE)
	public void stuExercise(JSONObject json) {
		try {
			long sqaId = json.getLong("sqaId");
			long studentId = json.getLong("studentId");
			long questionId = json.getLong("questionId");
			HomeworkAnswerResult result = json.getObject("result", HomeworkAnswerResult.class);
			studentExerciseQuestionStatService.updateExerciseQuestion(studentId, questionId, result, sqaId);
			studentExerciseQuestionStatService.updateExerciseQuestionKnowledge(studentId, questionId, result, sqaId);
		} catch (Exception e) {
			logger.error("student exercise: ", e);
		}
	}
}
