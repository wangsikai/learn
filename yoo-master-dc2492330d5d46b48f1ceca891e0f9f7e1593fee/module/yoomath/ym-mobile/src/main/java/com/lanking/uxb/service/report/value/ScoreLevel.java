package com.lanking.uxb.service.report.value;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 总成绩
 * 
 * @author wangsenhao
 *
 */
public enum ScoreLevel implements Valueable {
	/**
	 * 优秀
	 */
	EXCELLENT(0),
	/**
	 * 良好
	 */
	GOOD(1),
	/**
	 * 中等
	 */
	MEDIUM(2),
	/**
	 * 较差
	 */
	WEAK(3);

	private final int value;

	private ScoreLevel(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static ScoreLevel findByValue(int value) {
		switch (value) {
		case 0:
			return EXCELLENT;
		case 1:
			return GOOD;
		case 2:
			return MEDIUM;
		case 3:
			return WEAK;
		default:
			return null;
		}
	}
}
