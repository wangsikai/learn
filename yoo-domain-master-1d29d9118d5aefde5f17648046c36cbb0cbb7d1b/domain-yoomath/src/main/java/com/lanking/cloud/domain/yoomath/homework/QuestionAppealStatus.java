package com.lanking.cloud.domain.yoomath.homework;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 申诉状态.
 * 
 * @since 小优快批
 *
 */
public enum QuestionAppealStatus implements Valueable {

	/**
	 * 申诉发起/等待审核.
	 */
	INIT(0),

	/**
	 * 申诉成功.
	 */
	SUCCESS(1),

	/**
	 * 申诉失败.
	 */
	FAILURE(2);

	private final int value;

	private QuestionAppealStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static QuestionAppealStatus findByValue(int value) {
		switch (value) {
		case 1:
			return SUCCESS;
		case 2:
			return FAILURE;
		default:
			return INIT;
		}
	}
}
