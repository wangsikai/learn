package com.lanking.uxb.service.mall.value;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;

/**
 * 活动奖品商品
 * 
 * @since 3.0.2
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年12月23日
 */
public class VLotteryActivityGoods extends VGoods {

	private static final long serialVersionUID = -832751127804870125L;

	private CoinsGoodsType coinsGoodsType;

	public CoinsGoodsType getCoinsGoodsType() {
		return coinsGoodsType;
	}

	public void setCoinsGoodsType(CoinsGoodsType coinsGoodsType) {
		this.coinsGoodsType = coinsGoodsType;
	}

}
