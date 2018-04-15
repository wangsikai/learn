package com.lanking.uxb.service.data.api.impl.listener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDataRegistryConstants;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.uxb.service.data.api.HomeworkStudentClazzStatService;
import com.lanking.uxb.service.doQuestion.api.DoQuestionGoalService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;

/**
 * 充当着数据总线的作用
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月29日
 */
@Component
@Exchange(name = MqYoomathDataRegistryConstants.EX_YM_DATA)
public class YmDataMqListener {

	private Logger logger = LoggerFactory.getLogger(YmDataMqListener.class);

	@Autowired
	private HomeworkStudentClazzStatService hkStuClazzStatService;
	@Autowired
	private ZyStudentFallibleQuestionService sfqService;
	@Autowired
	private DoQuestionGoalService doQuestionGoalService;

	@Listener(queue = MqYoomathDataRegistryConstants.QUEUE_YM_DATA_HOMEWORKSTUDENTCLAZZSTAT, routingKey = MqYoomathDataRegistryConstants.RK_YM_DATA_HOMEWORKSTUDENTCLAZZSTAT)
	public void homeworkStudentClazzStat(JSONObject json) {
		try {
			hkStuClazzStatService.statisticDay30(json.getLongValue("classId"));
		} catch (Exception e) {
			logger.error("homework student clazz stat", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Listener(queue = MqYoomathDataRegistryConstants.QUEUE_YM_DATA_STUDENTFALLIBLE, routingKey = MqYoomathDataRegistryConstants.RK_YM_DATA_STUDENTFALLIBLE)
	public void stuExercise(JSONObject json) {
		try {
			long studentId = json.getLong("studentId");
			long questionId = json.getLong("questionId");
			HomeworkAnswerResult result = json.getObject("result", HomeworkAnswerResult.class);
			List<HomeworkAnswerResult> itemResults = json.getObject("itemResults", List.class);
			Map<Long, List<String>> latexAnswers = json.getObject("answers", Map.class);
			StudentQuestionAnswerSource source = json.getObject("source", StudentQuestionAnswerSource.class);
			Map<Long, List<String>> asciimathAnswers = json.getObject("answersAscii", Map.class);
			List<Long> answerImgs = json.getObject("answerImgs", List.class);
			if (answerImgs == null) {
				Long answerImg = json.getLong("answerImg");
				if (answerImg != null && answerImg > 0) {
					answerImgs = new ArrayList<Long>(1);
					answerImgs.add(answerImg);
				}
			}
			Integer rightRate = json.getInteger("rightRate");
			sfqService.update(questionId, studentId, result, itemResults, latexAnswers, asciimathAnswers, answerImgs,
					rightRate, source);
		} catch (Exception e) {
			logger.error("student fallible: ", e);
		}
	}

	// 临时(不能一个一个处理)
	@Listener(queue = MqYoomathDataRegistryConstants.QUEUE_YM_DATA_DOQUESTIONGOAL, routingKey = MqYoomathDataRegistryConstants.RK_YM_DATA_DOQUESTIONGOAL)
	public void doQuestionGoalCount(JSONObject json) {
		try {
			long studentId = json.getLong("studentId");
			StudentQuestionAnswerSource source = json.getObject("source", StudentQuestionAnswerSource.class);
			Date createAt = json.containsKey("createAt") ? json.getObject("createAt", Date.class) : new Date();
			if (source == StudentQuestionAnswerSource.DAILY_PRACTICE || source == StudentQuestionAnswerSource.FALLIBLE
					|| source == StudentQuestionAnswerSource.KNOWPOINT_EXERCISE
					|| source == StudentQuestionAnswerSource.SECTION_EXERCISE
					|| source == StudentQuestionAnswerSource.SMART_PAPER
					|| source == StudentQuestionAnswerSource.ENHANCE_PRACTICE
					|| source == StudentQuestionAnswerSource.HOMEWORK
					|| source == StudentQuestionAnswerSource.HOLIDAY_HOMEWORK) {
				long date0 = Long.valueOf(new SimpleDateFormat("yyyyMMdd").format(createAt));
				doQuestionGoalService.incrDoQuestionGoalCount(studentId, date0, 1);
			}
		} catch (Exception e) {
			logger.error("do question goal count:", e);
		}
	}

}
