package com.lanking.cloud.domain.frame.system;

import com.lanking.cloud.sdk.bean.Titleable;
import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 悠系列客户端
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum YooApp implements Valueable, Titleable {
	/**
	 * 悠数学学生端
	 */
	MATH_STUDENT(0, "学生端"),
	/**
	 * 悠数学教师端
	 */
	MATH_TEACHER(1, "教师端");

	private final int value;
	private final String title;

	private YooApp(int value, String title) {
		this.value = value;
		this.title = title;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String getTitle() {
		return title;
	}

	public static YooApp findByValue(int value) {
		switch (value) {
		case 0:
			return MATH_STUDENT;
		case 1:
			return MATH_TEACHER;
		default:
			return null;
		}
	}

}
