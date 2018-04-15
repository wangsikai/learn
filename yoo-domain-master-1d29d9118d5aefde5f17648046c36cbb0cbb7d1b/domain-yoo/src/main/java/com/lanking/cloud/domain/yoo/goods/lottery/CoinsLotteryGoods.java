package com.lanking.cloud.domain.yoo.goods.lottery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.domain.yoo.goods.Goods;

/**
 * 抽奖商品
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "coins_lo_goods")
public class CoinsLotteryGoods extends CoinsLotteryGoodsBaseInfo {
	private static final long serialVersionUID = -3389774507627314077L;

	/**
	 * 同goods中的ID {@link Goods}.id
	 */
	@Id
	private Long id;

	/**
	 * 快照ID {@link CoinsLotteryGoodsSnapshot}.id
	 */
	@Column(name = "coins_lo_goods_snapshot_id")
	private long coinsLotteryGoodsSnapshotId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCoinsLotteryGoodsSnapshotId() {
		return coinsLotteryGoodsSnapshotId;
	}

	public void setCoinsLotteryGoodsSnapshotId(long coinsLotteryGoodsSnapshotId) {
		this.coinsLotteryGoodsSnapshotId = coinsLotteryGoodsSnapshotId;
	}
}
