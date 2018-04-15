package com.lanking.uxb.service.honor.api.impl.listener;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.uxb.service.honor.api.TaskService;
import com.lanking.uxb.service.user.api.UserService;

/**
 * 用户任务MQ Listener
 *
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Component
@Exchange(name = MqHonorRegistryConstants.EX_TASK)
public class TaskMqListener {
	private static final Logger logger = LoggerFactory.getLogger(TaskMqListener.class);

	@Autowired
	private TaskService taskService;
	@Autowired
	private UserService userService;

	@Listener(queue = MqHonorRegistryConstants.QUEUE_TASK_LOG, routingKey = MqHonorRegistryConstants.RK_TASK_LOG)
	public void taskLog(JSONObject obj) {
		try {
			int taskCode = obj.getInteger("taskCode");
			long userId = obj.getLong("userId");
			Map<String, Object> params = new HashMap<String, Object>();
			if (obj.containsKey("params")) {
				JSONObject paramObj = obj.getJSONObject("params");
				params = new HashMap<String, Object>(paramObj.size());

				for (Map.Entry<String, Object> e : paramObj.entrySet()) {
					params.put(e.getKey(), e.getValue());
				}
			}

			// 如果外部不传isClient，就默认是客户端调用逻辑
			Boolean isClient = obj.getBoolean("isClient");
			if (isClient != null) {
				params.put("isClient", isClient);
			} else if (params.get("isClient") != null) {
				params.put("isClient", Boolean.parseBoolean(params.get("isClient").toString()));
			} else {
				params.put("isClient", true);
			}
			User user = userService.get(userId);
			if (user.getUserType() == UserType.STUDENT) {
				taskService.process(taskCode, userId, params);
			}
		} catch (Exception e) {
			logger.error("task error: {}", obj.toJSONString(), e);
		}
	}

	@Listener(queue = MqHonorRegistryConstants.QUEUE_TASK_LOG_101010001, routingKey = MqHonorRegistryConstants.RK_TASK_LOG_101010001)
	public void taskLog101010001(JSONObject obj) {
		try {
			int taskCode = obj.getInteger("taskCode");
			long userId = obj.getLong("userId");
			Map<String, Object> params = new HashMap<String, Object>();
			if (obj.containsKey("params")) {
				JSONObject paramObj = obj.getJSONObject("params");
				params = new HashMap<String, Object>(paramObj.size());

				for (Map.Entry<String, Object> e : paramObj.entrySet()) {
					params.put(e.getKey(), e.getValue());
				}
			}

			// 如果外部不传isClient，就默认是客户端调用逻辑
			Boolean isClient = obj.getBoolean("isClient");
			if (isClient != null) {
				params.put("isClient", isClient);
			} else if (params.get("isClient") != null) {
				params.put("isClient", Boolean.parseBoolean(params.get("isClient").toString()));
			} else {
				params.put("isClient", true);
			}
			User user = userService.get(userId);
			if (user.getUserType() == UserType.STUDENT) {
				taskService.process(taskCode, userId, params);
			}
		} catch (Exception e) {
			logger.error("task error: {}", obj.toJSONString(), e);
		}
	}
}
