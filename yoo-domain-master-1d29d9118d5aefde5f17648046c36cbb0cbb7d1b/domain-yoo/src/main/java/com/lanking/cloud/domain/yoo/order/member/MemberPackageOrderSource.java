package com.lanking.cloud.domain.yoo.order.member;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 会员套餐订单来源
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum MemberPackageOrderSource implements Valueable {
	/**
	 * 用户
	 */
	USER(0),
	/**
	 * 渠道
	 */
	CHANNEL(1);

	private int value;

	MemberPackageOrderSource(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

}
