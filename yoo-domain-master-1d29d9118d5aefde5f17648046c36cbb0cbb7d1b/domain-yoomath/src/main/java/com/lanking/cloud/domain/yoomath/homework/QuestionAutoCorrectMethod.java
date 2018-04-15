package com.lanking.cloud.domain.yoomath.homework;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 自动批改的方式方法.
 * 
 * @author wanlong.che
 *
 */
public enum QuestionAutoCorrectMethod implements Valueable {

	/**
	 * 自动判断，一般为空数据等.
	 */
	AUTO_CHECK(0),

	/**
	 * 对比历史批改结果.
	 */
	CONTRAST_HISTORY(1),

	/**
	 * 自动批改远程服务批改结果.
	 */
	CORRECT_SERVER(2);

	private final int value;

	private QuestionAutoCorrectMethod(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static QuestionAutoCorrectMethod findByValue(int value) {
		switch (value) {
		case 0:
			return AUTO_CHECK;
		case 1:
			return CONTRAST_HISTORY;
		case 2:
			return CORRECT_SERVER;
		default:
			return AUTO_CHECK;
		}
	}
}
