package com.lanking.uxb.service.homework.api.impl.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqYoomathCounterDetailRegistryConstants;
import com.lanking.cloud.component.mq.common.constants.MqYoomathHomeworkRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.uxb.service.resources.api.HomeworkQuestionService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;

@Component
@Exchange(name = MqYoomathHomeworkRegistryConstants.EX_YM_HOMEWORK)
public class MqHomeworkListener {

	private Logger logger = LoggerFactory.getLogger(MqHomeworkListener.class);

	@Autowired
	private MqSender mqSender;
	@Autowired
	private HomeworkQuestionService hqService;
	@Autowired
	private StudentHomeworkQuestionService shqService;

	/**
	 * 分发作业的时候处理
	 * 
	 * @param json
	 *            key:{teacherId,homeworkId}
	 */
	@Listener(queue = MqYoomathHomeworkRegistryConstants.QUEUE_YM_HOMEWORK_PUBLISH, routingKey = MqYoomathHomeworkRegistryConstants.RK_YM_HOMEWORK_PUBLISH)
	public void publish(JSONObject json) {
		try {
			long teacherId = json.getLongValue("teacherId");
			long homeworkId = json.getLongValue("homeworkId");
			List<Long> questionIds = hqService.getQuestion(homeworkId);
			for (Long questionId : questionIds) {
				JSONObject jo = new JSONObject();
				jo.put("bizId", questionId);
				jo.put("otherBizId", teacherId);
				jo.put("count", Count.COUNTER_1);
				jo.put("delta", 1);
				mqSender.send(MqYoomathCounterDetailRegistryConstants.EX_YM_COUNTERDETAIL,
						MqYoomathCounterDetailRegistryConstants.RK_YM_COUNTERDETAIL_QUESTIONUSER,
						MQ.builder().data(jo).build());
			}
		} catch (Exception e) {
			logger.error("publish error:", e);
		}
	}

	/**
	 * 提交作业的时候处理
	 * 
	 * @param json
	 *            key:{studentId,studentHomeworkId}
	 */
	@Listener(queue = MqYoomathHomeworkRegistryConstants.QUEUE_YM_HOMEWORK_COMMIT, routingKey = MqYoomathHomeworkRegistryConstants.RK_YM_HOMEWORK_COMMIT)
	public void commit(JSONObject json) {
		try {
			long studentId = json.getLongValue("studentId");
			long studentHomeworkId = json.getLongValue("studentHomeworkId");
			List<StudentHomeworkQuestion> shqs = shqService.find(studentHomeworkId);
			for (StudentHomeworkQuestion shq : shqs) {
				JSONObject jo = new JSONObject();
				jo.put("bizId", shq.getQuestionId());
				jo.put("otherBizId", studentId);
				jo.put("count", Count.COUNTER_1);
				jo.put("delta", 1);
				mqSender.send(MqYoomathCounterDetailRegistryConstants.EX_YM_COUNTERDETAIL,
						MqYoomathCounterDetailRegistryConstants.RK_YM_COUNTERDETAIL_QUESTIONUSER,
						MQ.builder().data(jo).build());
			}
		} catch (Exception e) {
			logger.error("commit error:", e);
		}
	}
}