package com.lanking.cloud.domain.common.resource.textbookExercise;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 预置作业类型
 * 
 * <pre>
 * 0:默认
 * 1:教学辅导
 * </pre>
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum TextbookExerciseType implements Valueable {
	/**
	 * 默认
	 */
	DEFAULT(0),
	/**
	 * 教辅
	 */
	TEACHING_COACH(1);

	private final int value;

	private TextbookExerciseType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static TextbookExerciseType findByValue(int value) {
		switch (value) {
		case 0:
			return DEFAULT;
		case 1:
			return TEACHING_COACH;
		default:
			return null;
		}
	}
}
