package com.lanking.cloud.domain.yoo.goods.activity.lottery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.domain.yoo.goods.Goods;

/**
 * 抽奖活动商品
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "lottery_activity_goods")
public class LotteryActivityGoods extends LotteryActivityGoodsBaseInfo {
	private static final long serialVersionUID = 150426826821951L;

	/**
	 * 同goods中的ID {@link Goods}.id
	 */
	@Id
	private Long id;

	/**
	 * 快照ID {@link LotteryActivityGoodsSnapshot}.id
	 */
	@Column(name = "snapshot_id")
	private long snapshotId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getSnapshotId() {
		return snapshotId;
	}

	public void setSnapshotId(long snapshotId) {
		this.snapshotId = snapshotId;
	}
}
