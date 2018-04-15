package com.lanking.uxb.security.api;

import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.uxb.core.annotation.ApiAllowed;

public final class SecurityContext {
	private static ThreadLocal<SecurityContext> contextHolder = new InheritableThreadLocal<SecurityContext>() {
		@Override
		protected SecurityContext initialValue() {
			return new SecurityContext();
		}
	};

	private Authentication authentication;
	private ApiAllowed apiAllowed;
	private long start;
	private String token;
	// 当前用户的角色
	private MemberType memberType;

	/**
	 * 小悠快批用户的ID.
	 */
	private Long correctUserId;

	public static Authentication getAuthentication() {
		return getContext().authentication;
	}

	public static void setAuthentication(Authentication authentication) {
		getContext().authentication = authentication;
	}

	public static ApiAllowed getApiAllowed() {
		return getContext().apiAllowed;
	}

	public static void setApiAllowed(ApiAllowed apiAllowed) {
		getContext().apiAllowed = apiAllowed;
	}

	public static long getStart() {
		return getContext().start;
	}

	public static void setStart(long start) {
		getContext().start = start;
	}

	public static String getToken() {
		return getContext().token;
	}

	public static void setToken(String token) {
		getContext().token = token;
	}

	public static MemberType getMemberType() {
		return getContext().memberType;
	}

	public static void setMemberType(MemberType memberType) {
		getContext().memberType = memberType;
	}

	public static Long getCorrectUserId() {
		return getContext().correctUserId;
	}

	public static void setCorrectUserId(Long correctUserId) {
		getContext().correctUserId = correctUserId;
	}

	public static SecurityContext getContext() {
		return contextHolder.get();
	}

	public static void clearContext() {
		contextHolder.remove();
	}
}
