package com.lanking.uxb.service.session.api.impl;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.sdk.util.Charsets;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.session.api.SessionProivder;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class SecurityFilter extends OncePerRequestFilter {

	private Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

	@Autowired
	private SessionProivder sessionProivder;

	private static String URI_ENCODING = Charsets.UTF8;
	private static String[] URI_IGNORES = null;

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		URI_ENCODING = Env.getString("server.tomcat.uri-encoding").intern();
		String uriIgnores = Env.getString("session.uri.ignores");
		if (StringUtils.isNotBlank(uriIgnores)) {
			URI_IGNORES = uriIgnores.split(",");
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		request.setCharacterEncoding(URI_ENCODING);
		String uri = request.getRequestURI().intern();
		boolean initSession = true;
		if (URI_IGNORES != null) {
			for (String ignore : URI_IGNORES) {
				if (uri.startsWith(ignore)) {
					initSession = false;
					break;
				}
			}
		}
		if (initSession || hasToken(request)) {
			SessionContext.setSession(sessionProivder.getSession(request, response));
			WebUtils.addCookie(request, response, Cookies.SECURITY_LOGIN_STATUS,
					Security.isLogin() ? "1".intern() : "0".intern());
		}
		logger.info("security filter uri:{} ", uri);
		filterChain.doFilter(request, response);
	}

	private boolean hasToken(HttpServletRequest request) {
		String token = request.getHeader(Cookies.SECURITY_TOKEN);
		if (StringUtils.isBlank(token)) {
			Cookie cookie = WebUtils.getCookie(request, Cookies.SECURITY_TOKEN);
			if (cookie != null) {
				token = cookie.getValue();
			}
		}
		if (StringUtils.isBlank(token)) {
			token = request.getParameter(Cookies.SECURITY_TOKEN);
		}
		return StringUtils.isNotBlank(token);
	}

}
