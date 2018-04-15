package com.lanking.uxb.service.interaction.api.impl.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqYoomathInteractionRegistryConstants;
import com.lanking.cloud.domain.yoomath.interaction.InteractionType;
import com.lanking.uxb.service.interaction.api.InteractionService;

@Component
@Exchange(name = MqYoomathInteractionRegistryConstants.EX_YM_INTERACTION)
public class InteractionMqListener {
	@Autowired
	private InteractionService interactionService;

	private Logger logger = LoggerFactory.getLogger(InteractionMqListener.class);

	@Listener(queue = MqYoomathInteractionRegistryConstants.QUEUE_YM_INTERACTION, routingKey = MqYoomathInteractionRegistryConstants.RK_YM_INTERACTION)
	public void interactionLog(JSONObject json) {
		try {
			Long homeworkId = json.getLongValue("homeworkId");
			Long classId = json.getLongValue("classId");
			InteractionType type = json.getObject("type", InteractionType.class);
			if (type == InteractionType.MOST_IMPROVED_STU) {
				interactionService.mostImprovedHandle(classId);
			} else if (type == InteractionType.ONE_HOMEWORK_TOP5) {
				interactionService.oneHomeworkTop5Handle(homeworkId);
			} else if (type == InteractionType.CLASS_HOMEWORK_TOP5) {
				interactionService.classHomeworkTop5Handle(classId);
			} else if (type == InteractionType.MOST_BACKWARD_STU) {
				interactionService.mostBackwardHandle(classId, null);
			} else if (type == InteractionType.SERIES_NOTSUBMIT_STU) {
				interactionService.seriesNotsubmitHandle(homeworkId);
			} else if (type == null) {
				interactionService.seriesNotsubmitHandle(homeworkId);
				List<Long> improveIds = interactionService.mostImprovedHandle(classId);
				interactionService.oneHomeworkTop5Handle(homeworkId);
				interactionService.mostBackwardHandle(classId, improveIds);
			}

		} catch (Exception e) {
			logger.error("interaction log:", e);
		}
	}
}
