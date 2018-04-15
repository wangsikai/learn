package com.lanking.cloud.domain.yoo.goods.lottery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 抽奖商品快照
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "coins_lo_goods_snapshot")
public class CoinsLotteryGoodsSnapshot extends CoinsLotteryGoodsBaseInfo {
	private static final long serialVersionUID = -4651093644796216043L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 关联抽奖商品ID {@link CoinsLotteryGoods}.id
	 */
	@Column(name = "coins_lo_goods_id")
	private long coinsLotteryGoodsId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCoinsLotteryGoodsId() {
		return coinsLotteryGoodsId;
	}

	public void setCoinsLotteryGoodsId(long coinsLotteryGoodsId) {
		this.coinsLotteryGoodsId = coinsLotteryGoodsId;
	}
}
