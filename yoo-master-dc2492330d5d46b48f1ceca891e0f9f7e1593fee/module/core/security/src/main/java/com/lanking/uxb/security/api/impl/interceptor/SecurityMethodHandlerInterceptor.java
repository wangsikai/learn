package com.lanking.uxb.security.api.impl.interceptor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.Authentication;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.security.config.SecurityConfig;
import com.lanking.uxb.security.type.SecurityConstants;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * Method Handler Interceptor（管控台不使用注解方式控制API权限）.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2014年12月5日
 */
public class SecurityMethodHandlerInterceptor extends HandlerInterceptorAdapter {
	private SecurityConfig securityConfig;
	private String loginUrl = "";

	public SecurityMethodHandlerInterceptor(SecurityConfig securityConfig) {
		this.securityConfig = securityConfig;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI().intern();
		PathMatcher matcher = new AntPathMatcher();
		for (String pattern : securityConfig.getIgnores()) {
			if (matcher.match(pattern, requestURI)) {
				return true;
			}
		}

		RolesAllowed rolesAllowed = ((HandlerMethod) handler).getMethodAnnotation(RolesAllowed.class);
		if (null == rolesAllowed) {
			rolesAllowed = AnnotationUtils.getAnnotation(((HandlerMethod) handler).getMethod().getDeclaringClass(),
					RolesAllowed.class);
			// 若都没有标注，则接口必须登录才能访问
			if (null == rolesAllowed && !Security.isLogin()) {
				this.loginUrl = securityConfig.getLoginPage();
				this.noPermission(request, response, true);
				return false;
			} else if (null == rolesAllowed && Security.isLogin()) {
				return true;
			}
		}

		// 如果 anyone = true，则未登录用户可以访问
		if (rolesAllowed.anyone()) {
			return true;
		}

		Authentication authentication = SecurityContext.getAuthentication();
		if (null != authentication && null != authentication.getAuthorities()) {
			for (String userType : rolesAllowed.userTypes()) {
				if (authentication.getAuthorities().contains(userType.toLowerCase())) {
					return true;
				}
			}
		}

		this.noPermission(request, response, !Security.isLogin());
		return false;
	}

	private void noPermission(HttpServletRequest request, HttpServletResponse response, boolean needLogin)
			throws ServletException, IOException {
		if (needLogin) {
			// 删除token
			WebUtils.removeCookie(request, response, Cookies.SECURITY_TOKEN);
		}
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && requestType.equals("XMLHttpRequest")) {
			response.getWriter().write(
					needLogin ? SecurityConstants.MSG_NEED_LOGIN_VALUE : SecurityConstants.MSG_NO_PERMISSION_VALUE);
		} else {
			if (Security.isClient()) {
				response.getWriter().write(
						needLogin ? SecurityConstants.MSG_NEED_LOGIN_VALUE : SecurityConstants.MSG_NO_PERMISSION_VALUE);
			} else {
				response.sendRedirect(this.loginUrl);
			}
		}
	}
}
