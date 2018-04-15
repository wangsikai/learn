package com.lanking.cloud.domain.yoo.goods.lottery;

import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSource;
import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 抽奖类型
 * 
 * @since 4.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年6月13日
 */
public enum CoinsLotteryType implements Valueable {
	/**
	 * 普通-对应订单来源的 {@link CoinsGoodsOrderSource}.LUCKY_DRAW
	 */
	COMMON(0),
	/**
	 * 假期活动01-对应订单来源的 {@link CoinsGoodsOrderSource}.HOLIDAY_ACTIVITY_01
	 */
	HOLIDAY_ACTIVITY_01(1);

	private int value;

	CoinsLotteryType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

}
