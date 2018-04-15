package com.lanking.cloud.domain.common.resource.book;

import com.lanking.cloud.sdk.bean.Valueable;

public enum BookCategory implements Valueable {
	/**
	 * 同步教辅
	 */
	TEACHING_ASSISTANT(0),
	/**
	 * 假期复习
	 */
	HOLIDAY_REVIEW(1),
	/**
	 * 总复习
	 */
	TOTAL_REVIEW(2);

	private int value;

	private BookCategory(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
