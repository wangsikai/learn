package com.lanking.uxb.service.push.api.impl;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleForm;
import com.lanking.uxb.service.push.api.PushHandle;
import com.lanking.uxb.service.push.api.PushHandleService;

@Service
public class PushHandleServiceImpl implements PushHandleService, ApplicationContextAware, InitializingBean {

	private ApplicationContext appContext;
	private List<PushHandle> handles = Lists.newArrayList();

	@Override
	public void push(PushHandleForm form) {
		for (PushHandle handle : handles) {
			if (handle.accept(form.getAction())) {
				handle.push(form);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (PushHandle handle : appContext.getBeansOfType(PushHandle.class).values()) {
			handles.add(handle);
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}

}
