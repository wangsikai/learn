package com.lanking.cloud.domain.yoo.goods.lottery;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 每期抽象商品被兑换数量计数
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public class CoinsLotterySeasonSellCountKey implements Serializable {
	private static final long serialVersionUID = 3778148684015341886L;

	/**
	 * 抽奖商品ID {@link CoinsLotteryGoods}.id
	 */
	@Id
	@Column(name = "coins_lo_goods_id", nullable = false)
	private long coinsLotteryGoodsId;

	/**
	 * 期数ID {@link CoinsLotterySeason}.id
	 */
	@Id
	@Column(name = "season_id", nullable = false)
	private long seasonId;

	public long getCoinsLotteryGoodsId() {
		return coinsLotteryGoodsId;
	}

	public void setCoinsLotteryGoodsId(long coinsLotteryGoodsId) {
		this.coinsLotteryGoodsId = coinsLotteryGoodsId;
	}

	public long getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(long seasonId) {
		this.seasonId = seasonId;
	}
}
