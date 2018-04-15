package com.lanking.cloud.domain.support.console.common;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 假期作业后台批改日志类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum HomeworkCorrectLogType implements Valueable {
	/**
	 * 普通作业
	 */
	HOMEWORK(0),

	/**
	 * 假期作业
	 */
	HOLIDAY_HOME(1);

	private final int value;

	HomeworkCorrectLogType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public HomeworkCorrectLogType findByValue(int value) {
		switch (value) {
		case 0:
			return HOMEWORK;
		case 1:
			return HOLIDAY_HOME;
		}

		return null;
	}
}
