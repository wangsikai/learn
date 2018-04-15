package com.lanking.uxb.service.wx.form;

import java.io.Serializable;

import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 绑定数据.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年12月30日
 */
public class BindForm implements Serializable {
	private static final long serialVersionUID = 7365191388835197300L;

	private String username;

	private String password;

	private String openid;

	private Long validtime;

	private String token;

	/**
	 * 需要绑定的账户类型.
	 */
	private UserType bindType;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Long getValidtime() {
		return validtime;
	}

	public void setValidtime(Long validtime) {
		this.validtime = validtime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserType getBindType() {
		return bindType;
	}

	public void setBindType(UserType bindType) {
		this.bindType = bindType;
	}
}
