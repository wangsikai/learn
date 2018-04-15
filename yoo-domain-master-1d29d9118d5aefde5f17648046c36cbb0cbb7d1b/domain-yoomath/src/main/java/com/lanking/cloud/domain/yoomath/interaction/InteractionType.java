package com.lanking.cloud.domain.yoomath.interaction;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 师生互动类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum InteractionType implements Valueable {
	/**
	 * 单次作业排名前5（班级）
	 */
	ONE_HOMEWORK_TOP5(0),
	/**
	 * 班级作业平均正确率进入TOP5
	 */
	CLASS_HOMEWORK_TOP5(1),
	/**
	 * 作业进步最快（班级）
	 */
	MOST_IMPROVED_STU(2),
	/**
	 * 作业退步最快（班级）
	 */
	MOST_BACKWARD_STU(3),
	/**
	 * 连续三次未提交作业
	 */
	SERIES_NOTSUBMIT_STU(4);

	private final int value;

	private InteractionType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static InteractionType findByValue(int value) {
		switch (value) {
		case 0:
			return ONE_HOMEWORK_TOP5;
		case 1:
			return CLASS_HOMEWORK_TOP5;
		case 2:
			return MOST_IMPROVED_STU;
		case 3:
			return MOST_BACKWARD_STU;
		case 4:
			return SERIES_NOTSUBMIT_STU;
		default:
			return null;
		}
	}
}
