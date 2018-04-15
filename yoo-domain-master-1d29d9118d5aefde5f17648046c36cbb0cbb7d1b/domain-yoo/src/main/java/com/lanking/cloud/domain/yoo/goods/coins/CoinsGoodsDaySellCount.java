package com.lanking.cloud.domain.yoo.goods.coins;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 金币商品被兑换的数量计数
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "coins_goods_day_sell_count")
@IdClass(CoinsGoodsDaySellCountKey.class)
public class CoinsGoodsDaySellCount extends CoinsGoodsDaySellCountKey {

	private static final long serialVersionUID = 3972894993750977076L;

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
