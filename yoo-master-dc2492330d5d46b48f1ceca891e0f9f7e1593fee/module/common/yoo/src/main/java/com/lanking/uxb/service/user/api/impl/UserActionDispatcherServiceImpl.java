package com.lanking.uxb.service.user.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.uxb.service.user.api.UserActionDispatcherService;
import com.lanking.uxb.service.user.api.UserActionProcessor;

/**
 * 用户行为处理器调度实现.
 * 
 * @author wlche
 *
 */
@Service
public class UserActionDispatcherServiceImpl implements UserActionDispatcherService, ApplicationContextAware {

	private boolean init = false;
	private ApplicationContext appContext;
	private List<UserActionProcessor> processors = Lists.newArrayList();; // 具体处理动作的业务集合

	@Override
	public void process(UserAction action, long userId, Map<String, Object> params) {
		if (action != null && userId > 0) {
			this.getProcessor(action, userId, params);
		}
	}

	/**
	 * 获得处理器并执行.
	 * 
	 * @param action
	 */
	void getProcessor(UserAction action, long userId, Map<String, Object> params) {
		this.initProcessor();
		for (UserActionProcessor processor : processors) {
			if (processor.accpet(action) && processor.on(action)) {
				processor.process(action, userId, params);
			}
		}
	}

	/**
	 * 初始化业务处理器.
	 */
	void initProcessor() {
		if (!init) {
			for (UserActionProcessor processor : appContext.getBeansOfType(UserActionProcessor.class).values()) {
				processors.add(processor);
			}
			init = true;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appContext = applicationContext;
	}
}
