package com.lanking.cloud.domain.common.baseData;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 考点频率
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum ExaminationPointFrequency implements Valueable {
	/**
	 * 高频
	 */
	HIGH(0),
	/**
	 * 一般(低频)
	 */
	LOW(1),
	/**
	 * 核心
	 */
	CORE(2);

	private final int value;

	private ExaminationPointFrequency(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static ExaminationPointFrequency findByValue(int value) {
		switch (value) {
		case 0:
			return HIGH;
		case 1:
			return LOW;
		case 2:
			return CORE;
		default:
			return null;
		}
	}
}
