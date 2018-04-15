package com.lanking.uxb.service.payment.cache;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 订单业务来源.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月11日
 */
public enum OrderBusinessSource implements Valueable {
	DEFAULT(0),

	/**
	 * 会员订单.
	 */
	USER_MEMBER(1),

	/**
	 * 学生错题代打印订单.
	 */
	STU_FALL_PRINT(2);

	private int value;

	OrderBusinessSource(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

}
