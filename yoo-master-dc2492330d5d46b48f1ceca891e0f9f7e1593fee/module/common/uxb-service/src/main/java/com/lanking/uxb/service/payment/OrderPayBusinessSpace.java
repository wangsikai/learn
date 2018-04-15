package com.lanking.uxb.service.payment;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 订单业务来源.<br>
 * 用于支付平台回调识别来源，不同业务实现时需在此处统一定义space name。
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月2日
 */
public enum OrderPayBusinessSpace implements Valueable {

	DEFAULT(0),

	/**
	 * 精品试卷
	 */
	EXAM_PAPER(1),

	/**
	 * 会员套餐
	 */
	MEMBER_PACKAGE(2),

	/**
	 * 错题本代打印
	 */
	STU_FALLIBLE_PRINT(3);

	private int value;

	OrderPayBusinessSpace(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static OrderPayBusinessSpace findByValue(int value) {
		switch (value) {
		case 1:
			return EXAM_PAPER;
		case 2:
			return MEMBER_PACKAGE;
		case 3:
			return STU_FALLIBLE_PRINT;
		default:
			return DEFAULT;
		}
	}
}
