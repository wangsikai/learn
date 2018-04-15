package com.lanking.uxb.service.account.form;

import com.lanking.cloud.domain.yoo.account.CredentialType;

/**
 * 第三方登录提交参数
 * 
 * @since yoomath(mobile) V1.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月24日
 */
public class ThirdPartyForm {
	// 第三方类型
	private CredentialType type;

	// QQ相关
	private String openId;
	private String accessToken;
	private long expireIn;
	private String payToken;
	private String pf;
	private String pfkey;
	private String msg;

	public CredentialType getType() {
		return type;
	}

	public void setType(CredentialType type) {
		this.type = type;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getExpireIn() {
		return expireIn;
	}

	public void setExpireIn(long expireIn) {
		this.expireIn = expireIn;
	}

	public String getPayToken() {
		return payToken;
	}

	public void setPayToken(String payToken) {
		this.payToken = payToken;
	}

	public String getPf() {
		return pf;
	}

	public void setPf(String pf) {
		this.pf = pf;
	}

	public String getPfkey() {
		return pfkey;
	}

	public void setPfkey(String pfkey) {
		this.pfkey = pfkey;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
