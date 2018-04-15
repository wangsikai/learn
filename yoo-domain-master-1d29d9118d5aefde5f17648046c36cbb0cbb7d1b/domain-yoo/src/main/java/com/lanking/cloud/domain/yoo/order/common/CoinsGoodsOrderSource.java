package com.lanking.cloud.domain.yoo.order.common;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 订单来源
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum CoinsGoodsOrderSource implements Valueable {
	/**
	 * 兑换
	 */
	EXCHANGE(0, "兑换"),
	/**
	 * 抽奖
	 */
	LUCKY_DRAW(1, "抽奖"),
	/**
	 * 单次抽奖活动
	 */
	LOTTERY_ACTIVITY(2, "活动"),
	/**
	 * 假期活动01
	 */
	HOLIDAY_ACTIVITY_01(3, "假期活动01");

	private final int value;
	private final String title;

	private CoinsGoodsOrderSource(int value, String title) {
		this.value = value;
		this.title = title;
	}

	public int getValue() {
		return value;
	}

	public String getTitle() {
		return title;
	}

	public static CoinsGoodsOrderSource findByValue(int value) {
		switch (value) {
		case 0:
			return EXCHANGE;
		case 1:
			return LUCKY_DRAW;
		case 2:
			return LOTTERY_ACTIVITY;
		case 3:
			return HOLIDAY_ACTIVITY_01;
		default:
			return null;
		}
	}
}
