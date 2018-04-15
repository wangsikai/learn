package com.lanking.cloud.domain.yoo.member;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 套餐组适用用户群类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum MemberPackageGroupType implements Valueable {
	/**
	 * 所有渠道用户
	 */
	ALL_CHANNEL_USER(0),
	/**
	 * 指定渠道下的用户
	 */
	CUSTOM_CHANNEL_USER(1),
	/**
	 * 注册用户
	 */
	REGISTER_USER(2);

	private int value;

	MemberPackageGroupType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static MemberPackageGroupType findByValue(int value) {
		switch (value) {
		case 0:
			return ALL_CHANNEL_USER;
		case 1:
			return CUSTOM_CHANNEL_USER;
		case 2:
			return REGISTER_USER;
		default:
			return null;
		}
	}

}
