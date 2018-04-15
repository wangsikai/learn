package com.lanking.cloud.sdk.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtils extends org.springframework.web.util.WebUtils {

	public static String getIPs(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String getRealIP(HttpServletRequest request) {
		return getIPs(request).split(",")[0];
	}

	public static String getUserAgent(HttpServletRequest request) {
		return request.getHeader("user-agent");
	}

	public static HttpServletResponse addCookie(HttpServletRequest request, HttpServletResponse response, String name,
			String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		response.addCookie(cookie);
		return response;
	}

	public static HttpServletResponse addDomainCookie(HttpServletRequest request, HttpServletResponse response,
			String name, String value, String domain) {
		Cookie cookie = new Cookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath("/");
		response.addCookie(cookie);
		return response;
	}

	public static HttpServletResponse addCookie(HttpServletRequest request, HttpServletResponse response, String name,
			String value, String path) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(path);
		response.addCookie(cookie);
		return response;
	}

	public static HttpServletResponse addDomainCookie(HttpServletRequest request, HttpServletResponse response,
			String name, String value, String domain, String path) {
		Cookie cookie = new Cookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath(path);
		response.addCookie(cookie);
		return response;
	}

	public static HttpServletResponse addCookie(HttpServletRequest request, HttpServletResponse response, String name,
			String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
		return response;
	}

	public static HttpServletResponse addDomainCookie(HttpServletRequest request, HttpServletResponse response,
			String name, String value, String domain, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
		return response;
	}

	public static HttpServletResponse addCookie(HttpServletRequest request, HttpServletResponse response, String name,
			String value, String path, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(path);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
		return response;
	}

	public static HttpServletResponse addDomainCookie(HttpServletRequest request, HttpServletResponse response,
			String name, String value, String domain, String path, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
		return response;
	}

	public static HttpServletResponse removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie cookie = getCookie(request, name);
		if (cookie != null) {
			addCookie(request, response, name, null, 0);
		}
		return response;
	}

	public static HttpServletResponse cleanCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookies[] = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookie != null) {
				addCookie(request, response, cookie.getName(), null, 0);
			}
		}
		return response;
	}

}
