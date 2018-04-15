package com.lanking.uxb.service.holiday.type;

/**
 * 布置作业选定范围 方便定义布置作业选定范围的树形展示
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
public enum HolidayPublishRangeType {
	TEXTBOOK(0),

	SECTION(1);

	private int value;

	HolidayPublishRangeType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
