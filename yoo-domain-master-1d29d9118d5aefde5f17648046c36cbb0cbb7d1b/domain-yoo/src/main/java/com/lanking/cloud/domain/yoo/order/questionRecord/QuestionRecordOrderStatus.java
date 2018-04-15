package com.lanking.cloud.domain.yoo.order.questionRecord;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 录题请求状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum QuestionRecordOrderStatus implements Valueable {
	/**
	 * 初始化待处理状态
	 */
	INIT(0),

	/**
	 * 已确认录题中
	 */
	WORKING(1),

	/**
	 * 录题完成
	 */
	COMPLETE(2),

	/**
	 * 联系不上
	 */
	NOT_CONTACT(3),

	/**
	 * 信息不符关闭需求
	 */
	CLOSE(4),

	/**
	 * 后台中止关闭
	 */
	TERMINATE(5);

	private int value;

	private QuestionRecordOrderStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static QuestionRecordOrderStatus findByValue(int value) {
		switch (value) {
		case 0:
			return INIT;
		case 1:
			return WORKING;
		case 2:
			return COMPLETE;
		case 3:
			return NOT_CONTACT;
		case 4:
			return CLOSE;
		case 5:
			return TERMINATE;
		default:
			return null;
		}
	}

}
