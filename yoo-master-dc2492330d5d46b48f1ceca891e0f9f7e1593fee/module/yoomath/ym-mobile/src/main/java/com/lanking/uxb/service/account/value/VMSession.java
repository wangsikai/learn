package com.lanking.uxb.service.account.value;

import java.io.Serializable;

import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.value.VUser;

/**
 * 悠数学移动客户端登录会话信息
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
public class VMSession implements Serializable {

	private static final long serialVersionUID = -7566797831774839560L;

	private String token;
	private VUser user;
	// 第三方登录后有没有进行第三方注册
	private boolean thirdPartyRegister = false;

	public VMSession() {
		super();
		this.token = Security.getToken();
	}

	public VMSession(VUser user) {
		super();
		this.token = Security.getToken();
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public VUser getUser() {
		return user;
	}

	public void setUser(VUser user) {
		this.user = user;
	}

	public boolean isThirdPartyRegister() {
		return thirdPartyRegister;
	}

	public void setThirdPartyRegister(boolean thirdPartyRegister) {
		this.thirdPartyRegister = thirdPartyRegister;
	}

}
