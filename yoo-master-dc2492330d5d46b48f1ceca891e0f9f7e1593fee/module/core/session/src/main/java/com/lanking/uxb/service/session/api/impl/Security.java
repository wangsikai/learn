package com.lanking.uxb.service.session.api.impl;

import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.base.session.api.SessionConstants;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Userable;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.session.api.Session;
import com.lanking.uxb.service.session.cache.VerifyCodeCacheService;
import com.lanking.uxb.service.session.form.VerifyCode;

@Component
public final class Security implements ApplicationContextAware {

	private static VerifyCodeCacheService verifyCodeCacheService;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		verifyCodeCacheService = applicationContext.getBean(VerifyCodeCacheService.class);
	}

	public static boolean hasSession() {
		return SessionContext.getSession() != null;
	}

	public static Session getSession() {
		return SessionContext.getSession();
	}

	public static com.lanking.cloud.domain.base.session.Session getInternalSession() {
		return getSession().getInternalSession();
	}

	public static long getSessionId() {
		return getSession().getId();
	}

	public static String getToken() {
		return getSession().getToken();
	}

	public static String getSafeToken() {
		if (getSession() == null) {
			return StringUtils.EMPTY;
		} else {
			return getToken();
		}
	}

	@SuppressWarnings("unused")
	private static Userable getUser() {
		return getSession().getUser();
	}

	public static long getSafeUserId() {
		Session session = getSession();
		if (session == null) {
			return SessionConstants.GUEST_ID;
		} else {
			return session.getUserId();
		}
	}

	public static long getUserId() {
		return getSession().getUserId();
	}

	public static long getAccountId() {
		return getSession().getAccountId();
	}

	public static boolean isLogin() {
		Session session = getSession();
		if (session == null) {
			return false;
		}
		return getSession().isLogin();
	}

	public static boolean isAdmin() {
		return getSession().isAdmin();
	}

	public static boolean isClientUser() {
		return getSession().isClientUser();
	}

	public static Set<Long> getRoles() {
		return getSession().getRoles();
	}

	public static UserType getUserType() {
		return getSession().getUserType();
	}

	public static UserType getSafeUserType() {
		Session session = getSession();
		if (session == null) {
			return UserType.NULL;
		}
		return session.getUserType();
	}

	public static boolean isClient() {
		return getDeviceType() != DeviceType.UNKNOWN && getDeviceType() != DeviceType.WEB;
	}

	public static boolean isWebView() {
		return getDeviceType() == DeviceType.ANDROID_WEBVIEW || getDeviceType() == DeviceType.IOS_WEBVIEW;
	}

	public static DeviceType getDeviceType() {
		Session session = getSession();
		if (session == null) {
			return DeviceType.UNKNOWN;
		}
		return session.getDeviceType();
	}

	public static String getVersion() {
		return getSession().getVersion();
	}

	public static String generateVerifyCode() {
		return verifyCodeCacheService.generateVerifyCode();
	}

	public static boolean checkVerifyCode(String code) {
		return verifyCodeCacheService.checkVerifyCode(code);
	}

	public static Product getLoginSource() {
		return getSession().getLoginSource();
	}

	public static VerifyCode getPointVerifyCodeOnly() {
		return verifyCodeCacheService.getPointVerifyCodeOnly();
	}

	public static VerifyCode getPointVerifyCode() {
		return verifyCodeCacheService.getPointVerifyCode();
	}

	public static void setPointVerifyCode(VerifyCode verifyCode) {
		verifyCodeCacheService.setPointVerifyCode(verifyCode);
	}

	public static boolean checkPointVerifyCode(String location) {
		return verifyCodeCacheService.checkPointVerifyCode(location);
	}

}
