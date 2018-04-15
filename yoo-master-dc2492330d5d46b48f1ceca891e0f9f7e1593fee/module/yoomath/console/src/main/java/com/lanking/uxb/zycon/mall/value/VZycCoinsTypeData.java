package com.lanking.uxb.zycon.mall.value;

import java.math.BigDecimal;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;

/**
 * 统计按类型的统计数据VO
 *
 * @author xinyu.zhou
 * @since 2.0.3
 */
public class VZycCoinsTypeData {
	private CoinsGoodsType type;
	private Long typeAmount;
	private BigDecimal typeTotalPrice;
	private String name;

	public CoinsGoodsType getType() {
		return type;
	}

	public void setType(CoinsGoodsType type) {
		this.type = type;
	}

	public Long getTypeAmount() {
		return typeAmount;
	}

	public void setTypeAmount(Long typeAmount) {
		this.typeAmount = typeAmount;
	}

	public BigDecimal getTypeTotalPrice() {
		return typeTotalPrice;
	}

	public void setTypeTotalPrice(BigDecimal typeTotalPrice) {
		this.typeTotalPrice = typeTotalPrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
