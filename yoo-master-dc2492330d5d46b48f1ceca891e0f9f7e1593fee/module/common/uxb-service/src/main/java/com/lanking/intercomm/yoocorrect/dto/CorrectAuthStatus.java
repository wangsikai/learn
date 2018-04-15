package com.lanking.intercomm.yoocorrect.dto;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 认证状态.
 * 
 * @since 小悠快批 v1.0
 *
 */
public enum CorrectAuthStatus implements Valueable {

	/**
	 * 待认证.
	 */
	DEFAULT(0),

	/**
	 * 待审核（审核中）.
	 */
	CHECK(1),

	/**
	 * 通过审核.
	 */
	PASS(2),

	/**
	 * 审核失败.
	 */
	FAIL(3);

	private int value;

	CorrectAuthStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static CorrectAuthStatus findByValue(int value) {
		switch (value) {
		case 1:
			return CHECK;
		case 2:
			return PASS;
		case 3:
			return FAIL;
		default:
			return DEFAULT;
		}
	}
}
