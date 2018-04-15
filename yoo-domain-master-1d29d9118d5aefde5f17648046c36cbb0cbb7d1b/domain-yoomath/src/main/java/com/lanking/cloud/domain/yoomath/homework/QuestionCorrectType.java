package com.lanking.cloud.domain.yoomath.homework;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 习题批改方式.
 * 
 * @author wanlong.che
 * @since 小优快批
 */
public enum QuestionCorrectType implements Valueable {

	/**
	 * 默认/初始化/未知.
	 */
	DEFAULT(0),

	/**
	 * 自动批改.
	 */
	AUTO_CORRECT(1),

	/**
	 * 人工批改（小悠快批）.
	 */
	YOO_CORRECT(2),

	/**
	 * 教师自行批改.
	 */
	TEACHER_CORRECT(3),

	/**
	 * 后台批改员批改.
	 */
	CONSOLE_CORRECT(4);

	private final int value;

	private QuestionCorrectType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static QuestionCorrectType findByValue(int value) {
		switch (value) {
		case 1:
			return AUTO_CORRECT;
		case 2:
			return YOO_CORRECT;
		case 3:
			return TEACHER_CORRECT;
		case 4:
			return CONSOLE_CORRECT;
		default:
			return DEFAULT;
		}
	}
}
