package com.lanking.cloud.domain.yoomath.clazz;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 班级来源
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum ClazzFrom implements Valueable {
	/**
	 * 默认来源.
	 */
	DEFAULT(0),

	/**
	 * 四川教育平台.
	 */
	SCEDU(1),

	/**
	 * 国家教育云
	 */
	EDUYUN(2),

	/**
	 * 融捷教育
	 */
	YOUNGYEDU(3);

	private final int value;

	private ClazzFrom(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static ClazzFrom findByValue(int value) {
		switch (value) {
		case 0:
			return DEFAULT;
		case 1:
			return SCEDU;
		case 2:
			return EDUYUN;
		case 3:
			return YOUNGYEDU;
		default:
			return DEFAULT;
		}
	}
}
