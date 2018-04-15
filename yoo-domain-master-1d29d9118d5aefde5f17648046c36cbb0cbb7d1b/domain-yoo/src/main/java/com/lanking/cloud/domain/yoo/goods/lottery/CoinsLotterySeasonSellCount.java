package com.lanking.cloud.domain.yoo.goods.lottery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 每期抽象商品被兑换数量计数
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "coins_lo_sean_sell_count")
@IdClass(CoinsLotterySeasonSellCountKey.class)
public class CoinsLotterySeasonSellCount extends CoinsLotterySeasonSellCountKey {
	private static final long serialVersionUID = 5318158823475129142L;

	/**
	 * 数量
	 */
	@Column(name = "count0")
	private Integer count0;

	public Integer getCount0() {
		return count0;
	}

	public void setCount0(Integer count0) {
		this.count0 = count0;
	}
}
