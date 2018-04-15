package com.lanking.cloud.job.paperReport.service;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 掌握情况
 * 
 * @author wangsenhao
 *
 */
public enum MasterStatus implements Valueable {
	/**
	 * 优秀
	 */
	EXCELLENT(0),
	/**
	 * 良好
	 */
	GOOD(1),
	/**
	 * 一般
	 */
	COMMONLY(2),
	/**
	 * 薄弱
	 */
	WEAK(3),
	/**
	 * 未练习
	 */
	NO_PRACTICE(4);

	private final int value;

	private MasterStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static MasterStatus findByValue(int value) {
		switch (value) {
		case 0:
			return EXCELLENT;
		case 1:
			return GOOD;
		case 2:
			return COMMONLY;
		case 3:
			return WEAK;
		case 4:
			return NO_PRACTICE;
		default:
			return null;
		}
	}
}
