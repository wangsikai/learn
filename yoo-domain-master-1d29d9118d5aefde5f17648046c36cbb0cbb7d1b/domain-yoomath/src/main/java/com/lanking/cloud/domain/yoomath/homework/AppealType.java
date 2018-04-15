package com.lanking.cloud.domain.yoomath.homework;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 申诉类型.
 *
 */
public enum AppealType implements Valueable {

	/**
	 * 非申诉题.
	 */
	DEFAULT(0),

	/**
	 * 批改错误.
	 */
	CORRECT_ERROR(1),

	/**
	 * 习题错误.
	 */
	QUESTION_ERROR(2);

	private int value;

	AppealType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static AppealType findByValue(int value) {
		switch (value) {
		case 1:
			return CORRECT_ERROR;
		case 2:
			return QUESTION_ERROR;
		default:
			return DEFAULT;
		}
	}
}
