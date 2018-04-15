package com.lanking.cloud.domain.yoomath.homework;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 作业留言场景.
 * 
 * @since 小优快批
 *
 */
public enum HomeworkMessageScene implements Valueable {

	/**
	 * 针对单个学生习题.
	 */
	STUDENT_SINGLE_QUESTION(0),

	/**
	 * 针对单个学生作业.
	 */
	STUDENT_SINGLE_HOMEWORK(1),

	/**
	 * 针对整份作业.
	 */
	HOMEWORK(2);

	private final int value;

	private HomeworkMessageScene(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static HomeworkMessageScene findByValue(int value) {
		switch (value) {
		case 0:
			return STUDENT_SINGLE_QUESTION;
		case 1:
			return STUDENT_SINGLE_HOMEWORK;
		case 2:
			return HOMEWORK;
		default:
			return STUDENT_SINGLE_QUESTION;
		}
	}
}
