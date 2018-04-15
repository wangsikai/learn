package com.lanking.cloud.domain.support.resources.question;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 纠错类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum QuestionErrorType implements Valueable {
	/**
	 * 题干
	 */
	CONTENT(0),
	/**
	 * 答案
	 */
	ANSWER(1),
	/**
	 * 提示
	 */
	HINT(2),
	/**
	 * 解析
	 */
	ANALYSIS(3);

	private int value;

	QuestionErrorType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static QuestionErrorType findByValue(int value) {
		switch (value) {
		case 0:
			return CONTENT;
		case 1:
			return ANSWER;
		case 2:
			return HINT;
		case 3:
			return ANALYSIS;
		default:
			return null;
		}
	}
}
