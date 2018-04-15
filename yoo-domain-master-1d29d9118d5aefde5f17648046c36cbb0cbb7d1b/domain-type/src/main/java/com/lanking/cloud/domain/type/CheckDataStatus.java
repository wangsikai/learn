package com.lanking.cloud.domain.type;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 审核状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum CheckDataStatus implements Valueable {
	/**
	 * 未校验.
	 */
	NOCHECK(0),
	/**
	 * 通过.
	 */
	PASS(1),
	/**
	 * 未通过（删除）.
	 */
	NOPASS(2),
	/**
	 * 标记.
	 */
	MARK(3);

	private final int value;

	private CheckDataStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static CheckDataStatus findByValue(int value) {
		switch (value) {
		case 0:
			return NOCHECK;
		case 1:
			return PASS;
		case 2:
			return NOPASS;
		case 3:
			return MARK;
		default:
			return NOCHECK;
		}
	}
}
