package com.lanking.uxb.security.api.impl.handler;

import java.io.IOException;
import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.lanking.cloud.sdk.value.MValue;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.service.session.api.impl.Security;

public class CustomHandlerMethodReturnValueHandler extends RequestResponseBodyMethodProcessor {

	public CustomHandlerMethodReturnValueHandler(List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return ((AnnotationUtils.findAnnotation(returnType.getContainingClass(), RestController.class) != null) || (returnType
				.getMethodAnnotation(RestController.class) != null));
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException {
		mavContainer.setRequestHandled(true);
		if (returnValue != null) {
			RestController RestController = returnType.getMethodAnnotation(RestController.class);
			if (RestController == null) {
				RestController = AnnotationUtils.findAnnotation(returnType.getContainingClass(), RestController.class);
			}
			if (RestController.token() || RestController.clearToken()) {
				if (returnValue.getClass().getName().equals(MValue.class.getName())) {
					if (RestController.clearToken()) {
						((MValue) returnValue).setRet_token("0");
					} else if (RestController.token()) {
						((MValue) returnValue).setRet_token(Security.getToken());
					}
				} else if (returnValue.getClass().getName().equals(Value.class.getName())) {
					if (RestController.clearToken()) {
						returnValue = new MValue("0", (Value) returnValue);
					} else if (RestController.token()) {
						returnValue = new MValue(Security.getToken(), (Value) returnValue);
					}
				}
			}
			writeWithMessageConverters(returnValue, returnType, webRequest);
		}
	}
}
