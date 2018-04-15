package com.lanking.cloud.domain.yoo.order;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 金币商品订单信息基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public abstract class GoodsOrderBaseInfo extends TradingOrderBaseInfo {

	private static final long serialVersionUID = 4769470559829695227L;

	/**
	 * 商品ID
	 */
	@Column(name = "goods_id")
	private long goodsId;

	/**
	 * 商品快照ID
	 */
	@Column(name = "goods_snapshot_id")
	private long goodsSnapshotId;

	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public long getGoodsSnapshotId() {
		return goodsSnapshotId;
	}

	public void setGoodsSnapshotId(long goodsSnapshotId) {
		this.goodsSnapshotId = goodsSnapshotId;
	}

}
