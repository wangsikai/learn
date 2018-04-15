package com.lanking.intercomm.yoocorrect.dto;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 批改习题的类别.
 *
 */
public enum CorrectQuestionCategory implements Valueable {

	/**
	 * 填空题（unknown）.
	 */
	BLANK_QUESTION_UNKNOW(0),

	/**
	 * 解答题.
	 */
	ANSWER_QUESTION(1),

	/**
	 * 填空题申诉.
	 */
	BLANK_QUESTION_APPEAL(2),

	/**
	 * 解答题申诉.
	 */
	ANSWER_QUESTION_APPEAL(3);

	private int value;

	CorrectQuestionCategory(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static CorrectQuestionCategory findByValue(int value) {
		switch (value) {
		case 1:
			return BLANK_QUESTION_UNKNOW;
		case 2:
			return ANSWER_QUESTION;
		case 3:
			return BLANK_QUESTION_APPEAL;
		default:
			return ANSWER_QUESTION_APPEAL;
		}
	}
}
