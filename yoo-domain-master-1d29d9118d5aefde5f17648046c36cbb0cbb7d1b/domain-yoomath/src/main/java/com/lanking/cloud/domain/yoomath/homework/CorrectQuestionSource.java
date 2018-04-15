package com.lanking.cloud.domain.yoomath.homework;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 习题来源.
 * 
 *
 */
public enum CorrectQuestionSource implements Valueable {

	/**
	 * 作业题.
	 */
	HOMEWORK(0),

	/**
	 * 订正题.
	 */
	AMEND(1),

	/**
	 * 自主练习.
	 */
	PRACTICE(2);

	private int value;

	CorrectQuestionSource(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static CorrectQuestionSource findByValue(int value) {
		switch (value) {
		case 1:
			return HOMEWORK;
		case 2:
			return AMEND;
		case 3:
			return PRACTICE;
		default:
			return HOMEWORK;
		}
	}
}
