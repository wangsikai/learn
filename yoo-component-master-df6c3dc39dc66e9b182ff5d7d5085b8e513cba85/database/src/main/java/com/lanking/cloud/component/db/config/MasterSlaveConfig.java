package com.lanking.cloud.component.db.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.lanking.cloud.component.db.masterslave.MasterSlaveInterceptor;

@Component
public class MasterSlaveConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MasterSlaveInterceptor());
		super.addInterceptors(registry);
	}

}
