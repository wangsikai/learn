package com.lanking.cloud.domain.yoo.order.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 金币兑换订单快照
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "coins_goods_order_snapshot")
public class CoinsGoodsOrderSnapshot extends CoinsGoodsOrderBaseInfo {

	private static final long serialVersionUID = 4607887332840699843L;

	/**
	 * 金币兑换订单ID
	 */
	@Column(name = "coins_goods_order_id")
	private long coinsGoodsOrderId;

	public long getCoinsGoodsOrderId() {
		return coinsGoodsOrderId;
	}

	public void setCoinsGoodsOrderId(long coinsGoodsOrderId) {
		this.coinsGoodsOrderId = coinsGoodsOrderId;
	}
}
