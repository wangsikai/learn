package com.lanking.uxb.service.user.api.impl.listener;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqUserActionRegistryConstants;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.uxb.service.honor.api.impl.listener.TaskMqListener;
import com.lanking.uxb.service.user.api.UserActionDispatcherService;

/**
 * 用户行为MQ消息监听.
 * 
 * @author wlche
 *
 */
@Component
@Exchange(name = MqUserActionRegistryConstants.EX_USER_ACTION)
public class UserActionListener {

	private static final Logger logger = LoggerFactory.getLogger(TaskMqListener.class);

	@Autowired
	private UserActionDispatcherService userActionDispatcherService;

	@Listener(queue = MqUserActionRegistryConstants.QUEUE_USER_ACTION_LOG, routingKey = MqUserActionRegistryConstants.RK_USER_ACTION_LOG)
	public void action(JSONObject obj) {
		try {
			int actionValue = obj.getInteger("actionValue");
			long userId = obj.getLong("userId");
			Map<String, Object> params = new HashMap<String, Object>();
			if (obj.containsKey("params")) {
				JSONObject paramObj = obj.getJSONObject("params");
				params = new HashMap<String, Object>(paramObj.size());

				for (Map.Entry<String, Object> e : paramObj.entrySet()) {
					params.put(e.getKey(), e.getValue());
				}
			}
			userActionDispatcherService.process(UserAction.findByValue(actionValue), userId, params);
		} catch (Exception e) {
			logger.error("user action listener error: {}", e);
		}
	}
}
