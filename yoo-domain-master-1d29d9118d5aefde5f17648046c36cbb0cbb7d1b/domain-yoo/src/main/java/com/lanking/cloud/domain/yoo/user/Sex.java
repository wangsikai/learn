package com.lanking.cloud.domain.yoo.user;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 性别
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum Sex implements Valueable {
	FEMALE(0), MALE(1), UNKNOWN(2);

	private int value;

	Sex(int value) {
		this.value = value;
	}

	public static String getCnName(Sex sex) {
		if (sex == FEMALE) {
			return "女";
		} else if (sex == MALE) {
			return "男";
		} else {
			return "";
		}
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static Sex findByValue(int value) {
		switch (value) {
		case 0:
			return FEMALE;
		case 1:
			return MALE;
		case 2:
			return UNKNOWN;
		default:
			return UNKNOWN;
		}
	}

	public static Sex findByName(String value) {
		if (value.equals("女")) {
			return Sex.FEMALE;
		} else if (value.equals("男")) {
			return Sex.MALE;
		} else {
			return Sex.UNKNOWN;
		}
	}
}
