package com.lanking.uxb.service.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.lanking.uxb.service.user.api.impl.interceptor.MemberAllowedInterceptor;

@Component
public class UserWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

	@Autowired
	private MemberAllowedInterceptor memberAllowedInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(memberAllowedInterceptor);
		super.addInterceptors(registry);
	}

}
