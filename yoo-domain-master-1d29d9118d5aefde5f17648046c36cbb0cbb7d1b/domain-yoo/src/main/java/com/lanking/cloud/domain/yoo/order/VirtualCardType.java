package com.lanking.cloud.domain.yoo.order;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 虚拟卡类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum VirtualCardType implements Valueable {

	/**
	 * 无.
	 */
	DEFAULT(0),

	/**
	 * 会员卡.
	 */
	MEMBER_PACKAGE(1);

	private int value;

	VirtualCardType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static VirtualCardType findByValue(int value) {
		switch (value) {
		case 0:
			return DEFAULT;
		case 1:
			return MEMBER_PACKAGE;
		default:
			return DEFAULT;
		}
	}
}
