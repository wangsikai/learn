package com.lanking.cloud.domain.type;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * ASCII 转换状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum AsciiStatus implements Valueable {
	/**
	 * 未转换.
	 */
	NOCHANGE(0),
	/**
	 * 已转换，未校验.
	 */
	NOCHECK(1),
	/**
	 * 已校验通过.
	 */
	PASS(2);

	private int value;

	AsciiStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static AsciiStatus findByValue(int value) {
		switch (value) {
		case 0:
			return NOCHANGE;
		case 1:
			return NOCHECK;
		case 2:
			return PASS;
		default:
			return NOCHANGE;
		}
	}
}
