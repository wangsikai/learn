package com.lanking.cloud.domain.common.baseData;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 重点难点
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum FocalDifficult implements Valueable {
	/**
	 * 一般
	 */
	NORMAL(0),
	/**
	 * 重点
	 */
	FOCAL(1),
	/**
	 * 难点
	 */
	DIFFICULT(2),
	/**
	 * 重点、难点
	 */
	FOCAL_DIFFICULT(3);

	private final int value;

	private FocalDifficult(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static FocalDifficult findByValue(int value) {
		switch (value) {
		case 0:
			return NORMAL;
		case 1:
			return FOCAL;
		case 2:
			return DIFFICULT;
		case 3:
			return FOCAL_DIFFICULT;
		default:
			return null;
		}
	}
}
