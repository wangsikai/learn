package com.lanking.uxb.service.resources.value;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 难度类型.
 * 
 * @author wlche
 *
 */
public enum DifficultType implements Valueable {
	/**
	 * 容易.
	 */
	EASY(0),
	/**
	 * 中等.
	 */
	MIDDLE(1),
	/**
	 * 困难.
	 */
	HARD(2);

	private final int value;

	private DifficultType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static DifficultType findByValue(int value) {
		switch (value) {
		case 0:
			return EASY;
		case 1:
			return MIDDLE;
		case 2:
			return HARD;
		default:
			return null;
		}
	}

	/**
	 * 根据难度值获取类型.
	 * 
	 * @param difficulty
	 *            难度值
	 * @return
	 */
	public static DifficultType findByDifficulty(double difficulty) {
		if (difficulty >= 0.8) {
			return EASY;
		} else if (difficulty < 0.8 && difficulty >= 0.4) {
			return MIDDLE;
		} else {
			return HARD;
		}
	}
}
