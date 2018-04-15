package com.lanking.cloud.domain.common.baseData;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 学校类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum SchoolType implements Valueable {
	NULL(0),
	/**
	 * 小学
	 */
	PRIMARY(1),
	/**
	 * 初中
	 */
	MIDDLE(2),
	/**
	 * 高中
	 */
	HIGH(3),
	/**
	 * 小初
	 */
	PRIMARY_MIDDLE(4),
	/**
	 * 初高
	 */
	MIDDLE_HIGH(5),
	/**
	 * 小初高
	 */
	PRIMARY_MIDDLE_HIGH(6);

	private final int value;

	private SchoolType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static SchoolType findByValue(int value) {
		switch (value) {
		case 0:
			return NULL;
		case 1:
			return PRIMARY;
		case 2:
			return MIDDLE;
		case 3:
			return HIGH;
		case 4:
			return PRIMARY_MIDDLE;
		case 5:
			return MIDDLE_HIGH;
		case 6:
			return PRIMARY_MIDDLE_HIGH;
		default:
			return null;
		}
	}

}
