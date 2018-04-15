package com.lanking.intercomm.yoocorrect.dto;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 用户类型
 */
public enum CorrectUserType implements Valueable {
	/**
	 * 教师
	 */
	TEACHER(0),
	/**
	 * 管理员
	 */
	ADMIN(1);

	private int value;

	CorrectUserType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static CorrectUserType findByValue(int value) {
		switch (value) {
		case 0:
			return TEACHER;
		case 1:
			return ADMIN;
		default:
			return TEACHER;
		}
	}

}
