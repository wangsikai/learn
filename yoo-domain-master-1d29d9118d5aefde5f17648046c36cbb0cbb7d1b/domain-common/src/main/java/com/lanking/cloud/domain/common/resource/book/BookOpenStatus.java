package com.lanking.cloud.domain.common.resource.book;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 书本开放状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum BookOpenStatus implements Valueable {
	/**
	 * 不开放
	 */
	NOT_OPEN(0),
	/**
	 * 开放
	 */
	OPEN(1),
	/**
	 * 针对学校开放
	 */
	SCHOOL_OPEN(2);

	private int value;

	BookOpenStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static BookOpenStatus findByValue(int value) {
		switch (value) {
		case 0:
			return NOT_OPEN;
		case 1:
			return OPEN;
		case 2:
			return SCHOOL_OPEN;
		default:
			return NOT_OPEN;
		}
	}
}
