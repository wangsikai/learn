package com.lanking.uxb.service.session.api.impl;

import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.base.session.SessionStatus;
import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.base.session.api.Headers;
import com.lanking.cloud.domain.base.session.api.SessionConstants;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Userable;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.util.UUID;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.session.api.Session;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionProivder;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.api.SessionUserService;
import com.lanking.uxb.service.session.cache.SessionCacheService;

@Component
public final class SessionProivderImpl implements SessionProivder, ApplicationContextAware, InitializingBean {

	@Autowired
	private SessionService sessionService;
	@Autowired
	private SessionCacheService sessionCacheService;

	private ApplicationContext appContext;

	private static String SESSION_TOKEN_PREFIX = null;
	private static String SESSION_TOKEN_SUFFIX = null;

	private List<SessionUserService> sessionUserServices = Lists.newArrayList();

	@Override
	public Session getSession(final HttpServletRequest request, final HttpServletResponse response) {
		final String token = getToken(request);
		MDC.put("TOKEN", token);
		AbstractSession s = new AbstractSession(token) {
			private com.lanking.cloud.domain.base.session.Session session;
			private Userable user;
			private Set<Long> roles;

			private SessionPacket init(String st) {
				setToken(st);
				session = new com.lanking.cloud.domain.base.session.Session();
				long currentTime = System.currentTimeMillis();
				session.setActiveAt(currentTime);
				session.setStartActiveAt(currentTime);
				session.setActiveTime(0);
				session.setAgent(WebUtils.getUserAgent(request));
				session.setCreateAt(currentTime);
				session.setLoginAt(0);
				session.setIp(WebUtils.getIPs(request));
				session.setStatus(SessionStatus.ACTIVE);
				session.setToken(st);
				session.setUserId(SessionConstants.GUEST_ID);
				session.setAccountId(SessionConstants.GUEST_ID);
				session.setUserType(UserType.NULL);
				sessionService.saveSession(session);
				SessionPacket packet = sessionService.createSessionPacket(session);
				sessionCacheService.setSessionPacket(packet);

				WebUtils.addCookie(request, response, Cookies.SECURITY_TOKEN, st);

				return packet;
			}

			@Override
			protected SessionPacket initPacket() {
				SessionPacket packet = null;
				if (token != null) {
					packet = sessionService.getSessionPacket(token);
				}
				if (packet == null || packet.getStatus() == SessionStatus.TIMEOUT
						|| packet.getStatus() == SessionStatus.LOGOUT) {
					packet = init(createToken());
				}
				// set device type per request
				packet.setDeviceType(parseDeviceType(request));
				// set version per request
				packet.setVersion(parseVersion(request));

				sessionCacheService.refresh(packet.getToken());
				sessionCacheService.setSessionPacket(packet);
				return packet;
			}

			@Override
			public Userable getUser() {
				if (user == null) {
					for (SessionUserService sessionUserService : sessionUserServices) {
						if (sessionUserService.getLoginUserType() == getPacket().getUserType()) {
							user = sessionUserService.getUser(getPacket().getUserId());
							break;
						}
					}
				}
				return user;
			}

			@Override
			public com.lanking.cloud.domain.base.session.Session getInternalSession() {
				if (session == null) {
					session = sessionService.getSession(getPacket().getToken());
				}
				return session;
			}

			@Override
			public Set<Long> getRoles() {
				if (roles == null) {
					for (SessionUserService sessionUserService : sessionUserServices) {
						if (sessionUserService.getLoginUserType() == getPacket().getUserType()) {
							roles = sessionUserService.getRoles(getPacket().getUserId());
							break;
						}
					}
				}
				return roles;
			}

		};
		s.getPacket();
		return s;
	}

	private DeviceType parseDeviceType(HttpServletRequest request) {
		String deviceType = request.getHeader(Headers.DEVICE_TYPE);
		if (StringUtils.isBlank(deviceType)) {
			Cookie cookie = WebUtils.getCookie(request, Headers.DEVICE_TYPE);
			if (cookie != null) {
				deviceType = cookie.getValue();
			}
			DeviceType dt = DeviceType.findByTitle(deviceType);
			if (dt == DeviceType.ANDROID) {
				return DeviceType.ANDROID_WEBVIEW;
			} else if (dt == DeviceType.IOS) {
				return DeviceType.IOS_WEBVIEW;
			} else {
				return dt;
			}
		} else {
			return DeviceType.findByTitle(deviceType);
		}
	}

	private String parseVersion(HttpServletRequest request) {
		String version = request.getHeader(Headers.VERSION);
		if (StringUtils.isBlank(version)) {
			version = request.getHeader(Headers._VERSION);
		}
		return version;
	}

	private String createToken() {
		return Codecs.md5Hex(String.valueOf(SESSION_TOKEN_PREFIX + UUID.uuid() + SESSION_TOKEN_SUFFIX));
	}

	private String getToken(HttpServletRequest request) {
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
		return token;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (SessionUserService sessionUserService : appContext.getBeansOfType(SessionUserService.class).values()) {
			sessionUserServices.add(sessionUserService);
		}
		SESSION_TOKEN_PREFIX = Env.getString("session.token.prefix");
		SESSION_TOKEN_SUFFIX = Env.getString("session.token.suffix");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appContext = applicationContext;
	}
}
