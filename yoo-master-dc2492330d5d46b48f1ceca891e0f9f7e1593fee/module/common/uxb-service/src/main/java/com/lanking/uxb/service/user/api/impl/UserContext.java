package com.lanking.uxb.service.user.api.impl;

import com.lanking.cloud.domain.yoo.user.UserInfo;

final class UserContext {

	private static ThreadLocal<UserContext> LOCAL = new InheritableThreadLocal<UserContext>() {
		@Override
		protected UserContext initialValue() {
			return new UserContext();
		}
	};

	private UserInfo userInfo;

	public static UserContext getContext() {
		return LOCAL.get();
	}

	public static void clearContext() {
		LOCAL.remove();
	}

	public static UserInfo getUserInfo() {
		return getContext().userInfo;
	}

	public static void setUserInfo(UserInfo userInfo) {
		getContext().userInfo = userInfo;
	}

	private UserContext() {
	}
}
