package com.lanking.cloud.domain.yoo.member;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 会员类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum MemberType implements Valueable {
	/**
	 * 非会员
	 */
	NONE(0),
	/**
	 * 会员
	 */
	VIP(1),
	/**
	 * 校级会员
	 */
	SCHOOL_VIP(2);

	private int value;

	MemberType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static MemberType findByValue(int value) {
		switch (value) {
		case 0:
			return NONE;
		case 1:
			return VIP;
		case 2:
			return SCHOOL_VIP;
		default:
			return null;
		}
	}

}
