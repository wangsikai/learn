package com.lanking.uxb.service.honor.api.impl.processor;

import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.uxb.service.honor.api.UserTaskProcessor;
import com.lanking.uxb.service.honor.api.UserTaskService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author xinyu.zhou
 * @since 4.1.0
 */
public abstract class AbstractUserTaskProcessor implements UserTaskProcessor {
	@Autowired
	private UserTaskService userTaskService;

	@Override
	public UserTask getUserTask(int code) {
		return userTaskService.get(code);
	}

	@Override
	public UserTask getUserTask(int code, long userId, Map<String, Object> params) {
		return getUserTask(code);
	}
}
