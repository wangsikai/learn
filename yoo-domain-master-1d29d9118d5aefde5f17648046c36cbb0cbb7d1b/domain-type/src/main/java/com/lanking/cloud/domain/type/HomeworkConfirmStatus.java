package com.lanking.cloud.domain.type;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 作业确认状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum HomeworkConfirmStatus implements Valueable {
	/**
	 * 不需要确认
	 */
	NOT_NEED_CONFIRM(0),
	/**
	 * 需要确认
	 */
	NEED_CONFIRM(1),
	/**
	 * 已经确认
	 */
	HAD_CONFIRM(2);

	private final int value;

	private HomeworkConfirmStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static HomeworkConfirmStatus findByValue(int value) {
		switch (value) {
		case 0:
			return NOT_NEED_CONFIRM;
		case 1:
			return NEED_CONFIRM;
		case 2:
			return HAD_CONFIRM;
		default:
			return null;
		}
	}
}
