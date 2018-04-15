package com.lanking.uxb.service.thirdparty;

import java.io.Serializable;

public class TokenInfo implements Serializable {
	private static final long serialVersionUID = -2231076219430616138L;

	/**
	 * TOKEN.
	 */
	private String token;

	/**
	 * 有效期.
	 */
	private Long validtime;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getValidtime() {
		return validtime;
	}

	public void setValidtime(Long validtime) {
		this.validtime = validtime;
	}
}
