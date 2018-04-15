package com.lanking.uxb.service.honor.api.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.honor.api.PointService;

@Service
public class PointServiceImpl implements PointService, ApplicationContextAware, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

	}
}
