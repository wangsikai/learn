package com.lanking.cloud.domain.yoo.goods.coins;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.lanking.cloud.domain.yoo.goods.Goods;

/**
 * 金币兑换商品
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "coins_goods")
public class CoinsGoods extends CoinsGoodsBaseInfo {

	private static final long serialVersionUID = -4814207511820055227L;

	/**
	 * 同goods的ID {@link Goods}.id
	 */
	@Id
	private Long id;

	/**
	 * 关联金币兑换商品最新快照ID {@link CoinsGoodsSnapshot}.id
	 */
	@Column(name = "coins_goods_snapshot_id")
	private long coinsGoodsSnapshotId;

	@Transient
	private boolean initDaySelledCount = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCoinsGoodsSnapshotId() {
		return coinsGoodsSnapshotId;
	}

	public void setCoinsGoodsSnapshotId(long coinsGoodsSnapshotId) {
		this.coinsGoodsSnapshotId = coinsGoodsSnapshotId;
	}

	public boolean isInitDaySelledCount() {
		return initDaySelledCount;
	}

	public void setInitDaySelledCount(boolean initDaySelledCount) {
		this.initDaySelledCount = initDaySelledCount;
	}

}
