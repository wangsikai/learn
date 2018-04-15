package com.lanking.uxb.service.thirdparty.weixin.response;

import java.io.Serializable;

import com.lanking.uxb.service.thirdparty.TokenInfo;

/**
 * 微信用户.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年12月30日
 */
public class WXUser implements Serializable {
	private static final long serialVersionUID = 117508918401570252L;

	/**
	 * TOKEN.
	 */
	private TokenInfo tokenInfo;

	/**
	 * 用户ID.
	 */
	private String personid;

	public TokenInfo getTokenInfo() {
		return tokenInfo;
	}

	public void setTokenInfo(TokenInfo tokenInfo) {
		this.tokenInfo = tokenInfo;
	}

	public String getPersonid() {
		return personid;
	}

	public void setPersonid(String personid) {
		this.personid = personid;
	}
}
