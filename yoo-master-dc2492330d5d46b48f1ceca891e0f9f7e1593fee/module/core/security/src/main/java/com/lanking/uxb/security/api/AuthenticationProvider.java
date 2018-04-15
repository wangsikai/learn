package com.lanking.uxb.security.api;

public interface AuthenticationProvider {

	/**
	 * 获得当前用户的 Authentication.
	 */
	Authentication getCurrentAuthentication();
}
