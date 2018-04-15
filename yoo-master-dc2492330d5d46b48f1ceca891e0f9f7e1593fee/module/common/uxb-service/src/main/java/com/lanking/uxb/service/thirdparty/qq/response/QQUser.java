package com.lanking.uxb.service.thirdparty.qq.response;

import java.io.Serializable;

import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.uxb.service.thirdparty.TokenInfo;

/**
 * QQ用户.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月19日
 */
public class QQUser implements Serializable {
	private static final long serialVersionUID = 5890436770472609546L;

	/**
	 * TOKEN.
	 */
	private TokenInfo tokenInfo;

	/**
	 * 用户ID.
	 */
	private String personid;

	/**
	 * 昵称.
	 */
	private String nickname;

	/**
	 * 头像.
	 */
	private String logourl;

	/**
	 * 性别
	 */
	private Sex sex;

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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getLogourl() {
		return logourl;
	}

	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}
}
