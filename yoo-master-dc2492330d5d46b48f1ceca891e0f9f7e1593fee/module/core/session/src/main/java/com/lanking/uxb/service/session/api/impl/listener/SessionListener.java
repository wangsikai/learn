package com.lanking.uxb.service.session.api.impl.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqSessionRegistryConstants;
import com.lanking.uxb.service.session.api.SessionService;

@Component
@Exchange(name = MqSessionRegistryConstants.EX_SESSION)
public class SessionListener {

	private Logger logger = LoggerFactory.getLogger(SessionListener.class);

	@Autowired
	private SessionService sessionService;

	@Listener(queue = MqSessionRegistryConstants.QUEUE_SESSION_OFFLINE, routingKey = MqSessionRegistryConstants.RK_SESSION_OFFLINE, series = false)
	public void offline(JSONObject jo) {
		try {
			Long userId = jo.getLong("userId");
			String excludeToken = jo.getString("excludeToken");
			sessionService.forceOffline(userId, excludeToken);
		} catch (Exception e) {
			logger.error("force offline error:{}", jo.toString(), e);
		}
	}
}
