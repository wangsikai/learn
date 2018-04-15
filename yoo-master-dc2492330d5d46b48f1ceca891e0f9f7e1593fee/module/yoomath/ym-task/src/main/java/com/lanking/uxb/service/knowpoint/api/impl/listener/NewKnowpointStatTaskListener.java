package com.lanking.uxb.service.knowpoint.api.impl.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDataRegistryConstants;
import com.lanking.uxb.service.knowpoint.api.HkClazzNewKnowpointStatService;
import com.lanking.uxb.service.knowpoint.api.HkStuClazzNewKnowpointStatService;

@Component
@Exchange(name = MqYoomathDataRegistryConstants.EX_YM_DATA)
public class NewKnowpointStatTaskListener {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private HkClazzNewKnowpointStatService hkClazzKnowpointStatService;
	@Autowired
	private HkStuClazzNewKnowpointStatService hkStuClazzKnowpointStatService;

	@Listener(queue = MqYoomathDataRegistryConstants.QUEUE_YM_DATA_HKCLAZZKNOWPOINTSTAT, routingKey = MqYoomathDataRegistryConstants.RK_YM_DATA_HKCLAZZKNOWPOINTSTAT)
	public void hkClazzKnowpointStat(JSONObject json) {
		try {
			hkClazzKnowpointStatService.statisticAfterHomework(json.getLongValue("homeworkId"));
		} catch (Exception e) {
			logger.error("homework student clazz stat", e);
		}
	}

	@Listener(queue = MqYoomathDataRegistryConstants.QUEUE_YM_DATA_HKSTUCLAZZKNOWPOINTSTAT, routingKey = MqYoomathDataRegistryConstants.RK_YM_DATA_HKSTUCLAZZKNOWPOINTSTAT)
	public void hkStuClazzKnowpointStat(JSONObject json) {
		try {
			hkStuClazzKnowpointStatService.statisticAfterHomework(json.getLongValue("homeworkId"));
		} catch (Exception e) {
			logger.error("homework student clazz stat", e);
		}
	}
}
