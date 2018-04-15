package com.lanking.cloud.domain.type;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 升级类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum UpgradeType implements Valueable {
	/**
	 * 可选
	 */
	OPTIONAL(0),
	/**
	 * 强制
	 */
	FORCE(1);

	private final int value;

	private UpgradeType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static UpgradeType findByValue(int value) {
		switch (value) {
		case 0:
			return OPTIONAL;
		case 1:
			return FORCE;
		default:
			return null;
		}
	}
}
