package com.lanking.cloud.domain.yoo.order.member;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 会员套餐订单类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum MemberPackageOrderType implements Valueable {
	/**
	 * 用户自己开通
	 */
	USER(0),
	/**
	 * 管理员开通
	 */
	ADMIN(1),
	/**
	 * 渠道管理员开通
	 */
	CHANNEL_ADMIN(2);

	private int value;

	MemberPackageOrderType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

}
