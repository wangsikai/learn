package com.lanking.cloud.domain.yoo.order;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 支付方式
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum PayMode implements Valueable {

	/**
	 * 免费
	 */
	FREE(0),

	/**
	 * 在线支付
	 */
	ONLINE(1),

	/**
	 * 金币支付
	 */
	COINS(2),

	/**
	 * 虚拟卡支付（会员卡、充值卡、兑换码等）
	 */
	VIRTUAL_CARD(3),

	/**
	 * 虚拟币支付（自定义货币如锤子，钥匙等）
	 */
	VIRTUAL_COINS(4);

	private int value;

	PayMode(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static PayMode findByValue(int value) {
		switch (value) {
		case 0:
			return FREE;
		case 1:
			return ONLINE;
		case 2:
			return COINS;
		case 3:
			return VIRTUAL_CARD;
		case 4:
			return VIRTUAL_COINS;
		default:
			return FREE;
		}
	}
}
