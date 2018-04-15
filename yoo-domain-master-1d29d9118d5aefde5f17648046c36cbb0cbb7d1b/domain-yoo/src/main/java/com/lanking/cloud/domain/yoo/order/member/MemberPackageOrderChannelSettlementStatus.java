package com.lanking.cloud.domain.yoo.order.member;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 会员套餐订单渠道结算状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum MemberPackageOrderChannelSettlementStatus implements Valueable {
	/**
	 * 未结算
	 */
	INIT(0),
	/**
	 * 已经结算
	 */
	SETTLED(1);

	private int value;

	MemberPackageOrderChannelSettlementStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

}
