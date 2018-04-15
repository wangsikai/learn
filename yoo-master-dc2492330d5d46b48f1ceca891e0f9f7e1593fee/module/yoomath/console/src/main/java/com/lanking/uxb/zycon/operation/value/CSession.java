package com.lanking.uxb.zycon.operation.value;

import java.io.Serializable;

/**
 * 管控台session VO
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年9月22日 下午7:57:01
 */
public class CSession implements Serializable {

	private static final long serialVersionUID = 1489196099074052775L;
	private long id;
	private Long userId;
	private Long accountId;
	private String accountName;
	private Long loginAt;
	private Long activeAt;
	private String ip;
	private String agent;
	private String token;
	private String device;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Long getLoginAt() {
		return loginAt;
	}

	public void setLoginAt(Long loginAt) {
		this.loginAt = loginAt;
	}

	public Long getActiveAt() {
		return activeAt;
	}

	public void setActiveAt(Long activeAt) {
		this.activeAt = activeAt;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

}
