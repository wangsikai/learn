package com.lanking.uxb.service.correct.vo;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 手动批改员类型.
 * 
 * @author wanlong.che
 *
 */
public enum CorrectorType implements Valueable {
	/**
	 * 教师.
	 */
	TEACHER(0),

	/**
	 * 后台批改员.
	 */
	PG_USER(1),

	/**
	 * 小优快批批改员.
	 */
	Y_CORRECTOR(2);

	private CorrectorType(int value) {
		this.value = value;
	}

	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static CorrectorType findByValue(int value) {
		switch (value) {
		case 0:
			return TEACHER;
		case 1:
			return PG_USER;
		case 2:
			return Y_CORRECTOR;
		default:
			return TEACHER;
		}
	}
}
