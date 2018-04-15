package com.lanking.cloud.domain.yoo.order;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 第三方支付平台.
 * 
 * @author wlche
 *
 */
public enum PaymentPlatform implements Valueable {
	/**
	 * 默认，无.
	 */
	DEFAULT(0),
	/**
	 * 微信.
	 */
	WECHAT(1),
	/**
	 * 支付宝.
	 */
	ALIPAY(2),
	/**
	 * 苹果应用内支付.
	 */
	IAP(3);

	private int value;

	PaymentPlatform(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static PaymentPlatform findByValue(int value) {
		switch (value) {
		case 0:
			return DEFAULT;
		case 1:
			return WECHAT;
		case 2:
			return ALIPAY;
		case 3:
			return IAP;
		default:
			return DEFAULT;
		}
	}
}
