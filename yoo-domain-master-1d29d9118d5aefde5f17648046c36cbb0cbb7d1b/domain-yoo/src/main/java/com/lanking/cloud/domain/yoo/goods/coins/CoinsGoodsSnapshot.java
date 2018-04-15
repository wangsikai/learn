package com.lanking.cloud.domain.yoo.goods.coins;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 金币兑换商品快照
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "coins_goods_snapshot")
public class CoinsGoodsSnapshot extends CoinsGoodsBaseInfo {

	private static final long serialVersionUID = 7373403639573610199L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 关联金币兑换的商品ID {@link CoinsGoods}.id
	 */
	@Column(name = "coins_goods_id")
	private long coinsGoodsId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCoinsGoodsId() {
		return coinsGoodsId;
	}

	public void setCoinsGoodsId(long coinsGoodsId) {
		this.coinsGoodsId = coinsGoodsId;
	}

}
