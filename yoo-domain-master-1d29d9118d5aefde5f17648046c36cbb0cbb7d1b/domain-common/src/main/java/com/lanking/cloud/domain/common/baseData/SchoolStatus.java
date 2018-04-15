package com.lanking.cloud.domain.common.baseData;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 学校状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum SchoolStatus implements Valueable {
	/**
	 * 未开通
	 */
	NOT_OPEN(0),
	/**
	 * 开通
	 */
	OPEN(1),
	/**
	 * 关闭
	 */
	CLOSE(2);

	private final int value;

	private SchoolStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static SchoolStatus findByValue(int value) {
		switch (value) {
		case 0:
			return NOT_OPEN;
		case 1:
			return OPEN;
		case 2:
			return CLOSE;
		default:
			return null;
		}
	}

}
