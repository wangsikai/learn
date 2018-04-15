package com.lanking.cloud.domain.yoo.goods.lottery;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 每期抽奖&奖品的对应关系
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public class CoinsLotterySeasonGoodsKey implements Serializable {
	private static final long serialVersionUID = 4116750075403291154L;

	/**
	 * 商品ID {@link CoinsLotteryGoods}.id
	 */
	@Id
	@Column(name = "coins_lo_goods_id", nullable = false)
	private Long coinsLotteryGoodsId;

	/**
	 * 期别ID {@link CoinsLotterySeason}.id
	 */
	@Id
	@Column(name = "season_id", nullable = false)
	private Long seasonId;

	public CoinsLotterySeasonGoodsKey(long coinsLotteryGoodsId, long seasonId) {
		this.coinsLotteryGoodsId = coinsLotteryGoodsId;
		this.seasonId = seasonId;
	}

	public CoinsLotterySeasonGoodsKey() {
	}

	public Long getCoinsLotteryGoodsId() {
		return coinsLotteryGoodsId;
	}

	public void setCoinsLotteryGoodsId(Long coinsLotteryGoodsId) {
		this.coinsLotteryGoodsId = coinsLotteryGoodsId;
	}

	public Long getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(Long seasonId) {
		this.seasonId = seasonId;
	}
}
