package com.lanking.uxb.ycoorect.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class YooCorrectWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

	@Autowired
	private LoadCorrectUserInterceptor memberAllowedInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(memberAllowedInterceptor);
		super.addInterceptors(registry);
	}
}
