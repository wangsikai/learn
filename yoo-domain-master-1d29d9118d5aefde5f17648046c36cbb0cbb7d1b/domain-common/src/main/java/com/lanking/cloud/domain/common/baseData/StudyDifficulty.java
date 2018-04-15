package com.lanking.cloud.domain.common.baseData;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 学习要求
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum StudyDifficulty implements Valueable {
	/**
	 * A
	 */
	A(0),
	/**
	 * B
	 */
	B(1),
	/**
	 * C
	 */
	C(2);

	private final int value;

	private StudyDifficulty(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static StudyDifficulty findByValue(int value) {
		switch (value) {
		case 0:
			return A;
		case 1:
			return B;
		case 2:
			return C;
		default:
			return null;
		}
	}
}
