package com.lanking.cloud.domain.yoo.order.questionRecord;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 题目录入请求类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum QuestionRecordOrderType implements Valueable {
	/**
	 * 请求类型为教辅图书
	 */
	BOOK(0),

	/**
	 * 请求类型为录入题目
	 */
	QUESTION(1);

	private int value;

	private QuestionRecordOrderType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static QuestionRecordOrderType findByValue(int value) {
		switch (value) {
		case 0:
			return BOOK;
		case 1:
			return QUESTION;
		default:
			return null;
		}
	}
}
