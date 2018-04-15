package com.lanking.uxb.service.honor.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStatus;
import com.lanking.uxb.service.honor.api.TaskService;
import com.lanking.uxb.service.honor.api.UserTaskProcessor;

/**
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService, ApplicationContextAware, InitializingBean {
	private ApplicationContext appContext;
	List<UserTaskProcessor> processors = Lists.newArrayList();

	@Override
	@Transactional
	public void process(int taskCode, long userId, Map<String, Object> params) {
		if (taskCode > 0) {
			UserTaskProcessor processor = getProcessor(taskCode);
			UserTask userTask = processor.getUserTask(taskCode, userId, params);
			if (userTask != null && userTask.getStatus() == UserTaskStatus.OPEN) {
				processor.process(userTask, userId, params);
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}

	UserTaskProcessor getProcessor(int code) {
		UserTaskProcessor processor = null;
		for (UserTaskProcessor p : processors) {
			if (p.accept(code)) {
				processor = p;
			}
		}
		return processor;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (UserTaskProcessor processor : appContext.getBeansOfType(UserTaskProcessor.class).values()) {
			processors.add(processor);
		}
	}

}
