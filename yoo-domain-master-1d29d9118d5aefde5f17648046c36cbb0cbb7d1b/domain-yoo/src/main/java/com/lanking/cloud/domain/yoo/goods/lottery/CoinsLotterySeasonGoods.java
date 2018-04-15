package com.lanking.cloud.domain.yoo.goods.lottery;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 每期抽奖&奖品的对应关系
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "coins_lo_season_goods")
@IdClass(CoinsLotterySeasonGoodsKey.class)
public class CoinsLotterySeasonGoods extends CoinsLotterySeasonGoodsKey {

	private static final long serialVersionUID = -6822452332979767493L;

	/**
	 * 每期奖品数量
	 */
	@Column(name = "sell_count")
	private Integer sellCount;

	/**
	 * 中奖概率
	 */
	@Column(name = "awards_rate", scale = 2)
	private BigDecimal awardsRate;

	/**
	 * 每期排序顺序
	 */
	@Column(name = "sequence", precision = 3)
	private Integer sequence;

	public CoinsLotterySeasonGoods(long coinsLotteryGoodsId, long seasonId) {
		super(coinsLotteryGoodsId, seasonId);
	}

	public CoinsLotterySeasonGoods() {
	}

	public Integer getSellCount() {
		return sellCount;
	}

	public void setSellCount(Integer sellCount) {
		this.sellCount = sellCount;
	}

	public BigDecimal getAwardsRate() {
		return awardsRate;
	}

	public void setAwardsRate(BigDecimal awardsRate) {
		this.awardsRate = awardsRate;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
