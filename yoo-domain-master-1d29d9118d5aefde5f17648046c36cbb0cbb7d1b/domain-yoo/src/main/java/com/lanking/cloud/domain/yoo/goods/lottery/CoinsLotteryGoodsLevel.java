package com.lanking.cloud.domain.yoo.goods.lottery;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 抽奖奖品等级
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum CoinsLotteryGoodsLevel implements Valueable {
	/**
	 * 初级奖品
	 */
	PRIMARY(0),

	/**
	 * 常规奖品
	 */
	REGULAR(1),

	/**
	 * 中级奖品
	 */
	MEDIUM(2),

	/**
	 * 大奖
	 */
	TOP(3),

	/**
	 * 谢谢参与
	 */
	NOTHING(4);

	private int value;

	CoinsLotteryGoodsLevel(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static CoinsLotteryGoodsLevel findByValue(int value) {
		switch (value) {
		case 0:
			return PRIMARY;
		case 1:
			return REGULAR;
		case 2:
			return MEDIUM;
		case 3:
			return TOP;
		case 4:
			return NOTHING;
		default:
			return null;
		}
	}
}
