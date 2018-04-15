package com.lanking.cloud.domain.yoomath.holidayHomework;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 假日作业类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum HolidayHomeworkType implements Valueable {
	/**
	 * 寒假
	 */
	WINTER_VACATION(0);

	private final int value;

	private HolidayHomeworkType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static HolidayHomeworkType findByValue(int value) {
		switch (value) {
		case 0:
			return WINTER_VACATION;
		default:
			return null;
		}
	}
}
