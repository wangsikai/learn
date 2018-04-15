package com.lanking.uxb.service.data.api.impl.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqYoomathPushRegistryConstants;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleForm;
import com.lanking.uxb.service.push.api.PushHandleService;

@Component
@Exchange(name = MqYoomathPushRegistryConstants.EX_YM_PUSH)
public class YmPushMqListener {

	private Logger logger = LoggerFactory.getLogger(YmPushMqListener.class);

	@Autowired
	private PushHandleService pushHandleService;

	@Listener(queue = MqYoomathPushRegistryConstants.QUEUE_YM_PUSH, routingKey = MqYoomathPushRegistryConstants.RK_YM_PUSH)
	public void yoomathPush(PushHandleForm form) {
		try {
			pushHandleService.push(form);
		} catch (Exception e) {
			logger.error("yoomath push:", e);
		}
	}

}
