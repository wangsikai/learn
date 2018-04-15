package com.lanking.cloud.domain.type;

import org.apache.commons.lang3.EnumUtils;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 作业答案结果
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum HomeworkAnswerResult implements Valueable {
	/**
	 * 初始化
	 */
	INIT(0),
	/**
	 * 正确
	 */
	RIGHT(1),
	/**
	 * 错误
	 */
	WRONG(2),
	/**
	 * 未知
	 */
	UNKNOW(3);

	private int value;

	HomeworkAnswerResult(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static HomeworkAnswerResult findByValue(int value) {
		switch (value) {
		case 0:
			return INIT;
		case 1:
			return RIGHT;
		case 2:
			return WRONG;
		case 3:
			return UNKNOW;
		default:
			return null;
		}
	}

	public static HomeworkAnswerResult findByName(String name) {
		return EnumUtils.getEnum(HomeworkAnswerResult.class, name);
	}
}
