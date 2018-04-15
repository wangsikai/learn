package com.lanking.uxb.service.user.api.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.uxb.service.user.api.UserActionProcessor;
import com.lanking.uxb.service.user.api.UserActionService;

/**
 * 用户统一的动作行为调用接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月22日
 */
@Service
public class UserActionServiceImpl implements UserActionService, ApplicationContextAware {

	private boolean init = false;
	private ApplicationContext appContext;
	private List<UserActionProcessor> processors = Lists.newArrayList();; // 具体处理动作的业务集合

	@Autowired
	private MqSender mqSender;

	@Override
	public void action(UserAction action, long userId, Map<String, Object> params) {
		if (action != null && userId > 0) {
			this.getProcessor(action, userId, params == null ? new HashMap<String, Object>(0) : params);
		}
	}

	@Override
	public void asyncAction(UserAction action, long userId, Map<String, Object> params) {
		if (action != null && userId > 0) {
			JSONObject messageObj = new JSONObject();
			messageObj.put("userId", userId);
			messageObj.put("actionValue", action.getValue());
			messageObj.put("params", params);
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
