package com.lanking.intercomm.yoocorrect.dto;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 奖励类型.
 *
 */
public enum CorrectRewardType implements Valueable {

	DEFAULT(0),

	/**
	 * 每日奖励.
	 */
	DAY(1);

	private int value;

	CorrectRewardType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static CorrectRewardType findByValue(int value) {
		switch (value) {
		case 1:
			return DAY;
		default:
			return DEFAULT;
		}
	}
}
