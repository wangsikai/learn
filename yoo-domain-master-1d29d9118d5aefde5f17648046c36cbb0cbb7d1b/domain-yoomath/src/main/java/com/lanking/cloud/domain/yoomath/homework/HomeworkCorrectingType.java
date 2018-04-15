package com.lanking.cloud.domain.yoomath.homework;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 作业批改类型
 * 
 * @since 3.9.3
 * @since 小优快批，2018-2-24 不再有学生批改动作
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Deprecated
public enum HomeworkCorrectingType implements Valueable {
	/**
	 * 教师批改
	 */
	TEACHER(0),
	/**
	 * 学生批改
	 */
	STUDENT(1);

	private final int value;

	private HomeworkCorrectingType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static HomeworkCorrectingType findByValue(int value) {
		switch (value) {
		case 0:
			return TEACHER;
		case 1:
			return STUDENT;
		default:
			return null;
		}
	}
}
