package com.lanking.cloud.domain.yoomath.homework;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 作业类型.
 * 
 * @author wanlong.che
 *
 */
public enum HomeworkType implements Valueable {
	/**
	 * 普通作业
	 */
	HOMEWORK(0),

	/**
	 * 假期作业
	 */
	HOLIDAY_HOME(1);

	private final int value;

	HomeworkType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public HomeworkType findByValue(int value) {
		switch (value) {
		case 0:
			return HOMEWORK;
		case 1:
			return HOLIDAY_HOME;
		}

		return null;
	}
}
