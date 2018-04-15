package com.lanking.uxb.security.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.lanking.uxb.security.api.impl.handler.CustomHandlerMethodReturnValueHandler;

@Configuration
public class ReturnValueHandleConfig extends WebMvcConfigurerAdapter {

	@Autowired
	@Qualifier("fastJsonHttpMessageConverter")
	private AbstractHttpMessageConverter<Object> httpMessageConverter;

	private List<HttpMessageConverter<?>> messageConverters;

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		this.messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(httpMessageConverter);
		returnValueHandlers.add(new CustomHandlerMethodReturnValueHandler(messageConverters));
	}

}
