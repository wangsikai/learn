package com.lanking.cloud.domain.yoo.member;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 会员兑换卡状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum MemberPackageCardStatus implements Valueable {

	/**
	 * 正常
	 */
	DEFAULT(0),

	/**
	 * 冻结（手动禁用状态）
	 */
	DISABLE(1),

	/**
	 * 废弃(已使用)
	 */
	DELETE(2);

	private int value;

	MemberPackageCardStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static MemberPackageCardStatus findByValue(int value) {
		switch (value) {
		case 0:
			return DEFAULT;
		case 1:
			return DISABLE;
		case 2:
			return DELETE;
		default:
			return DEFAULT;
		}
	}
}
