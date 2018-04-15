package com.lanking.cloud.domain.yoo.goods.lottery;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;

/**
 * 金币抽奖商品信息基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public abstract class CoinsLotteryGoodsBaseInfo implements Serializable {
	private static final long serialVersionUID = -9092940130648136759L;

	/**
	 * 金币商品类型
	 */
	@Column(name = "coins_goods_type", precision = 3)
	private CoinsGoodsType coinsGoodsType;

	/**
	 * 抽奖奖品等级
	 */
	@Column(name = "level", precision = 3)
	private CoinsLotteryGoodsLevel level;

	public CoinsGoodsType getCoinsGoodsType() {
		return coinsGoodsType;
	}

	public void setCoinsGoodsType(CoinsGoodsType coinsGoodsType) {
		this.coinsGoodsType = coinsGoodsType;
	}

	public CoinsLotteryGoodsLevel getLevel() {
		return level;
	}

	public void setLevel(CoinsLotteryGoodsLevel level) {
		this.level = level;
	}
}
