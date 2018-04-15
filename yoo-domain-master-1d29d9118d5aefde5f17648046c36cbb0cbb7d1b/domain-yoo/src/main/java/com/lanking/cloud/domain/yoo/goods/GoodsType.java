package com.lanking.cloud.domain.yoo.goods;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 商品类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum GoodsType implements Valueable {
	/**
	 * 金币商城商品
	 */
	COINS_MALL(0),
	/**
	 * 金币抽奖商品
	 */
	COINS_LOTTERY(1),
	/**
	 * 资源商品
	 */
	RESOURCES(2),
	/**
	 * 抽奖活动商品
	 */
	LOTTERY_ACTIVITY(3);

	private int value;

	GoodsType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static GoodsType findByValue(int value) {
		switch (value) {
		case 0:
			return COINS_MALL;
		case 1:
			return COINS_LOTTERY;
		case 2:
			return RESOURCES;
		case 3:
			return LOTTERY_ACTIVITY;
		default:
			return null;
		}
	}
}
