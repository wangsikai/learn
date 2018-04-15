package com.lanking.cloud.domain.yoo.account;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 第三方凭证类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum CredentialType implements Valueable {
	DEFAULT(0),
	/**
	 * 教育云.
	 */
	EDUYUN(1),
	/**
	 * QQ.
	 */
	QQ(2),
	/**
	 * 微信.
	 */
	WEIXIN(3),
	/**
	 * 微博.
	 */
	WEIBO(4),
	/**
	 * 四川教育平台.
	 */
	SCEDU(5),
	/**
	 * 微信公众平台.
	 */
	WEIXIN_MP(6),
	/**
	 * 江苏电信教育平台.
	 */
	JSEDU(7),
	/**
	 * 融捷教育.
	 */
	YOUNGY_EDU(8),
	/**
	 * 九龙中学.
	 */
	JLMS(9);

	private final int value;

	private CredentialType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static CredentialType findByValue(int value) {
		switch (value) {
		case 0:
			return DEFAULT;
		case 1:
			return EDUYUN;
		case 2:
			return QQ;
		case 3:
			return WEIXIN;
		case 4:
			return WEIBO;
		case 5:
			return SCEDU;
		case 6:
			return WEIXIN_MP;
		case 7:
			return JSEDU;
		case 8:
			return YOUNGY_EDU;
		case 9:
			return JLMS;
		default:
			return DEFAULT;
		}
	}
}
