package com.lanking.cloud.component.db.masterslave;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class MasterSlaveInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		RestController RestController = AnnotationUtils
				.getAnnotation(((HandlerMethod) handler).getMethod().getDeclaringClass(), RestController.class);
		if (RestController != null && RestController instanceof RestController) {
			MasterSlaveDataSource MasterSlaveDataSource = ((HandlerMethod) handler)
					.getMethodAnnotation(MasterSlaveDataSource.class);
			if (MasterSlaveDataSource != null) {
				MasterSlaveContext.set(MasterSlaveDataSource.type());
			} else {
				MasterSlaveContext.set("M");
			}
		}
		return true;
	}
}
