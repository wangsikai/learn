package com.lanking.uxb.service.mall.value;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoodsLevel;

/**
 * 抽奖奖品
 *
 * @author xinyu.zhou
 * @since 2.4.0
 */
public class VCoinsLotteryGoods extends VGoods {
	private static final long serialVersionUID = 6681893811116672247L;

	private long coinsGoodsSnapshotId;
	private CoinsLotteryGoodsLevel level;
	private CoinsGoodsType coinsGoodsType;

	public long getCoinsGoodsSnapshotId() {
		return coinsGoodsSnapshotId;
	}

	public void setCoinsGoodsSnapshotId(long coinsGoodsSnapshotId) {
		this.coinsGoodsSnapshotId = coinsGoodsSnapshotId;
	}

	public CoinsLotteryGoodsLevel getLevel() {
		return level;
	}

	public void setLevel(CoinsLotteryGoodsLevel level) {
		this.level = level;
	}

	public CoinsGoodsType getCoinsGoodsType() {
		return coinsGoodsType;
	}

	public void setCoinsGoodsType(CoinsGoodsType coinsGoodsType) {
		this.coinsGoodsType = coinsGoodsType;
	}
}
