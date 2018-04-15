package com.lanking.uxb.security.api.impl.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 融捷api过滤相关接口
 *
 * @author xinyu.zhou
 * @since 3.9.3
 */
public class YoungySecurityMethodHandlerInterceptor extends HandlerInterceptorAdapter {
	private static final String FILTER_URL = "/router/youngyedu/**";
	private static final String NOT_FILTER_URL = "/router/youngyedu/ym/sync/**";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI().intern();
		PathMatcher matcher = new AntPathMatcher();
		if (matcher.match(NOT_FILTER_URL, requestURI)) {
			return true;
		}
		boolean needFilter = false;
		if (matcher.match(FILTER_URL, requestURI)) {
			needFilter = true;
		}

		if (!needFilter) {
			return true;
		}

		String subject = request.getHeader("SUBJECT");
		if (StringUtils.isBlank(subject) || !subject.equals("math")) {
			return false;
		}
		String deviceType = request.getHeader("DEVICE_TYPE");
		if (StringUtils.isBlank(deviceType) || !deviceType.equals("INK_SCREEN")) {
			return false;
		}
		String version = request.getHeader("VERSION");
		if (StringUtils.isBlank(version)) {
			return false;
		}
		String app = request.getHeader("APP");
		if (StringUtils.isBlank(app) || !app.equals("MATH_STUDENT")) {
			return false;
		}
		String tp = request.getHeader("THIRD_PARTY");

		return !(StringUtils.isBlank(tp) || !tp.equals("youngyedu"));
	}
}
