package com.lanking.cloud.domain.yoo.order;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 第三方支付方式
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum ThirdPaymentMethod implements Valueable {

	/**
	 * 默认（无）
	 */
	DEFAULT(0),

	/**
	 * 微信公众号支付
	 */
	WX_JSAPI(1),

	/**
	 * 微信原生扫码支付
	 */
	WX_NATIVE(2),

	/**
	 * 微信APP支付
	 */
	WX_APP(3),

	/**
	 * 刷卡支付.
	 */
	WX_MICROPAY(4);

	private int value;

	ThirdPaymentMethod(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static ThirdPaymentMethod findByValue(int value) {
		switch (value) {
		case 0:
			return DEFAULT;
		case 1:
			return WX_JSAPI;
		case 2:
			return WX_NATIVE;
		case 3:
			return WX_APP;
		case 4:
			return WX_MICROPAY;
		default:
			return DEFAULT;
		}
	}
}
