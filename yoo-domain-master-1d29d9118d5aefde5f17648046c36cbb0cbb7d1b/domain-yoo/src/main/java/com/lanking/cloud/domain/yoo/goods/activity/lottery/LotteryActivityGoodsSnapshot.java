package com.lanking.cloud.domain.yoo.goods.activity.lottery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 抽奖活动商品快照
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "lottery_activity_goods_snapshot")
public class LotteryActivityGoodsSnapshot extends LotteryActivityGoodsBaseInfo {
	private static final long serialVersionUID = 6257969397548928257L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 抽奖活动商品ID {@link LotteryActivityGoods}.id
	 */
	@Column(name = "goods_id")
	private long goodsId;

	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
