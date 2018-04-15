package com.lanking.cloud.domain.yoo.goods.activity.lottery;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 抽奖活动使用的虚拟币类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum LotteryActivityVirtualCoinsType implements Valueable {

	/**
	 * 无（免费抽奖）
	 */
	DEFUALT(0),

	/**
	 * 金币.
	 */
	COINS(1),

	/**
	 * 自定义虚拟币（如锤子、钥匙等）.
	 */
	CUSTOM(2);

	private int value;

	LotteryActivityVirtualCoinsType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static LotteryActivityVirtualCoinsType findByValue(int value) {
		switch (value) {
		case 0:
			return DEFUALT;
		case 1:
			return COINS;
		case 2:
			return CUSTOM;
		default:
			return DEFUALT;
		}
	}
}
