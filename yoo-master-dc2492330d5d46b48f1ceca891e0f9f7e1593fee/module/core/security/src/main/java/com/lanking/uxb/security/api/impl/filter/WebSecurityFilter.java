package com.lanking.uxb.security.api.impl.filter;

import httl.util.StringUtils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lanking.uxb.security.api.Authentication;
import com.lanking.uxb.security.api.AuthenticationProvider;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.security.config.SecurityConfig;
import com.lanking.uxb.security.type.SecurityConstants;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * URI filter.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2014年12月5日
 */
@Component
@ConditionalOnExpression("${safe.enable}")
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class WebSecurityFilter extends OncePerRequestFilter {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SecurityConfig securityConfig;
	@Autowired
	private AuthenticationProvider securityProvider;

	private String loginUrl = "";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		SecurityContext.clearContext();
		String requestURI = request.getRequestURI().intern();
		PathMatcher matcher = new AntPathMatcher();
		// logger.debug("[Filter] >>>>>-requestURI: {}", requestURI);

		// Ignores URIs，放行
		for (String pattern : securityConfig.getIgnores()) {
			if (matcher.match(pattern, requestURI)) {
				filterChain.doFilter(request, response);
				return;
			}
		}

		boolean hasPrefix = false;
		for (String prefix : securityConfig.getPrefixs()) {
			if (matcher.match(prefix, requestURI)) {
				hasPrefix = true;
				this.commonFilter(request, response, filterChain);
				break;
			}
		}

		if (!hasPrefix) {
			this.webFilter(request, response, filterChain); // web
		}
	}

	/**
	 * web.
	 * 
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */
	private void webFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if (Security.isLogin()) {
			Authentication authentication = securityProvider.getCurrentAuthentication();
			SecurityContext.setAuthentication(authentication);
			if (null != authentication.getRelativeURIs() && authentication.getRelativeURIs().size() > 0) {
				for (String relativeURI : authentication.getRelativeURIs()) {
					if (StringUtils.isNotBlank(relativeURI) && requestURI.endsWith(relativeURI)) {
						filterChain.doFilter(request, response);
						return;
					}
				}
				// 无URI权限
				this.noPermission(request, response, false);
				return;
			}
		} else {
			SecurityContext.setAuthentication(null);
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * commonFilter.
	 * 
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */
	private void commonFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		this.setAuthentication();
		filterChain.doFilter(request, response);
	}

	/**
	 * Authentication存储.
	 */
	private void setAuthentication() {
		if (Security.isLogin()) {
			Authentication authentication = securityProvider.getCurrentAuthentication();
			SecurityContext.setAuthentication(authentication);
		}
	}

	private void noPermission(HttpServletRequest request, HttpServletResponse response, boolean needLogin)
			throws ServletException, IOException {
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