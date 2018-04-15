package com.lanking.intercomm.yoocorrect.dto;

import com.lanking.cloud.sdk.bean.Valueable;

public enum WithdrawStatus implements Valueable {

	/**
	 * 无.
	 */
	INIT(0),

	/**
	 * 提现中（审核中）.
	 */
	CHECK(1),

	/**
	 * 提现成功.
	 */
	SUCCESS(2),

	/**
	 * 提现失败.
	 */
	FAILURE(3);

	private int value;

	WithdrawStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static WithdrawStatus findByValue(int value) {
		switch (value) {
		case 1:
			return CHECK;
		case 2:
			return SUCCESS;
		case 3:
			return FAILURE;
		default:
			return INIT;
		}
	}
}
