package com.lanking.cloud.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqActivityRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.job.nationalDayActivity.service.StudentDoQuestionService;
import com.lanking.cloud.sdk.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Exchange(name = MqActivityRegistryConstants.EX_NDA01)
public class NationalDayActivity01DQInternalListener {

	@Autowired
	@Qualifier("nda01StudentDoQuestionService")
	private StudentDoQuestionService stuDQService;
	@Autowired
	private MqSender mqSender;

	void pushExMq(long studentId, long questionId, long doAt) {
		JSONObject exjo = new JSONObject();
		exjo.put("studentId", studentId);
		exjo.put("questionId", questionId);
		exjo.put("doAt", doAt);
		mqSender.asynSend(MqActivityRegistryConstants.EX_NDA01, MqActivityRegistryConstants.RK_NDA01_DQ,
				MQ.builder().data(exjo).build());
	}

	@SuppressWarnings("unchecked")
	void internalDq(JSONObject jo) {
		log.info("do question right count:{}", jo.toString());
		long studentId = jo.getLongValue("studentId");
		Long questionId = jo.getLong("questionId");
		long doAt = jo.getLongValue("doAt");
		if (questionId != null) {// 单个题目
			try {
				stuDQService.doQuestionRightCount(studentId, questionId, doAt);
			} catch (Exception e) {
				pushExMq(studentId, questionId, doAt);
				log.info("do question count error,push mq again", e);
			}
		} else {// 多个题目处理
			List<Long> questionIds = jo.getObject("questionIds", List.class);
			if (CollectionUtils.isNotEmpty(questionIds)) {
				for (Long qId : questionIds) {
					try {
						stuDQService.doQuestionRightCount(studentId, qId, doAt);
					} catch (Exception e) {
						pushExMq(studentId, qId, doAt);
						log.info("do question count error,push mq again", e);
					}
				}
			}
		}
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_DQ_0, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_0)
	public void dq0(JSONObject jo) {
		internalDq(jo);
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_DQ_1, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_1)
	public void dq1(JSONObject jo) {
		internalDq(jo);
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_DQ_2, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_2)
	public void dq2(JSONObject jo) {
		internalDq(jo);
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_DQ_3, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_3)
	public void dq3(JSONObject jo) {
		internalDq(jo);
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_DQ_4, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_4)
	public void dq4(JSONObject jo) {
		internalDq(jo);
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_DQ_5, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_5)
	public void dq5(JSONObject jo) {
		internalDq(jo);
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_DQ_6, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_6)
	public void dq6(JSONObject jo) {
		internalDq(jo);
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_DQ_7, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_7)
	public void dq7(JSONObject jo) {
		internalDq(jo);
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_DQ_8, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_8)
	public void dq8(JSONObject jo) {
		internalDq(jo);
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_DQ_9, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_9)
	public void dq9(JSONObject jo) {
		internalDq(jo);
	}

	@Listener(queue = MqActivityRegistryConstants.QUEUE_NDA01_DQ_10, routingKey = MqActivityRegistryConstants.RK_NDA01_DQ_10)
	public void dq10(JSONObject jo) {
		internalDq(jo);
	}
}
