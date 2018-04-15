package com.lanking.cloud.domain.common.baseData;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 敏感词类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum SensitiveType implements Valueable {
	NULL(0),
	/**
	 * 禁止
	 */
	FORBIDDEN(1),
	/**
	 * 替换
	 */
	REPLACE(2),
	/**
	 * 审核
	 */
	AUDIT(3);

	private final int value;

	private SensitiveType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static SensitiveType findByValue(int value) {
		switch (value) {
		case 0:
			return NULL;
		case 1:
			return FORBIDDEN;
		case 2:
			return REPLACE;
		case 3:
			return AUDIT;
		default:
			return null;
		}
	}

}
