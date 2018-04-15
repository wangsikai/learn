package com.lanking.uxb.service.adminSecurity.api.impl.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.support.common.auth.ConsoleRole;
import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.service.adminSecurity.api.ConsoleRoleService;
import com.lanking.uxb.service.adminSecurity.api.ConsoleUserService;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;
import com.lanking.uxb.service.session.api.impl.Security;

public class AdminSecurityMethodHandlerInterceptor extends HandlerInterceptorAdapter {
	private ConsoleRoleService consoleRoleService;
	private ConsoleUserService consoleUserService;
	private List<String> filterUrls;

	private static final int NEED_LOGIN_ERROR_CODE = 501;

	public AdminSecurityMethodHandlerInterceptor(ConsoleRoleService consoleRoleService,
			ConsoleUserService consoleUserService, List<String> filterUrls) {
		this.consoleUserService = consoleUserService;
		this.consoleRoleService = consoleRoleService;
		this.filterUrls = filterUrls;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String requestURI = request.getRequestURI().intern();
		PathMatcher matcher = new AntPathMatcher();
		boolean needFilter = false;
		for (String pattern : filterUrls) {
			if (matcher.match(pattern, requestURI)) {
				needFilter = true;
				break;
			}
		}

		if (!needFilter) {
			return true;
		}
		ConsoleRolesAllowed rolesAllowed = ((HandlerMethod) handler).getMethodAnnotation(ConsoleRolesAllowed.class);

		if (null != rolesAllowed && rolesAllowed.anyone()) {
			return true;
		}

		// 若未配置则直接可以访问
		if (null == rolesAllowed && Security.isLogin()) {
			return true;
		}

		if (!Security.isLogin()) {
			noPermission(request, response, true);
			return false;
		}

		// 超级管理员直接可以访问
		ConsoleUser consoleUser = consoleUserService.get(Security.getUserId());
		if (consoleUser.getSystemId() == 0) {
			return true;
		}
		if (rolesAllowed.systemAdmin() && consoleUser.getSystemId() != 0) {
			noPermission(request, response, !Security.isLogin());
			return false;
		}

		List<ConsoleRole> consoleRoles = consoleRoleService.getUserRoles(Security.getUserId());
		Map<String, ConsoleRole> consoleRoleMap = new HashMap<String, ConsoleRole>(consoleRoles.size());
		for (ConsoleRole consoleRole : consoleRoles) {
			consoleRoleMap.put(consoleRole.getCode(), consoleRole);
		}

		for (String roleCode : rolesAllowed.roleCodes()) {
			if (consoleRoleMap.get(roleCode) != null) {
				return true;
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
			response.getWriter().write(JSONObject.toJSON(new Value(NEED_LOGIN_ERROR_CODE, "need login")).toString());
		} else {
			response.getWriter().write(JSONObject.toJSON(new Value(new NoPermissionException())).toString());
		}

	}
}
