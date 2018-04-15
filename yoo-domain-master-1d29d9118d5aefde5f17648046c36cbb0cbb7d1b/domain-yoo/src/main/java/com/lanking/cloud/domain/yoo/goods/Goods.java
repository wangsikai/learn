package com.lanking.cloud.domain.yoo.goods;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.yoo.goods.activity.lottery.LotteryActivityGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoods;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;

/**
 * 商品
 * 
 * <pre>
 * 所有类型商品在此表中都有对应的记录： {@link GoodsType}
 * 1.活动商品： {@link LotteryActivityGoods}
 * 2.金币商品： {@link CoinsGoods}
 * 3.抽奖商品： {@link CoinsLotteryGoods}
 * 4.资源商品： {@link ResourcesGoods}
 * </pre>
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "goods")
public class Goods extends GoodsBaseInfo {

	private static final long serialVersionUID = -6784983687218096065L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	// 关联商品最新快照ID
	@Column(name = "goods_snapshot_id")
	private long goodsSnapshotId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getGoodsSnapshotId() {
		return goodsSnapshotId;
	}

	public void setGoodsSnapshotId(long goodsSnapshotId) {
		this.goodsSnapshotId = goodsSnapshotId;
	}

}
