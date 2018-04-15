package com.lanking.uxb.service.mall.value;

import java.io.Serializable;
import java.util.List;

/**
 * Value for CoinsGoodsGroup
 *
 * @author xinyu.zhou
 * @since 3.9.3
 */
public class VCoinsGoodsGroup implements Serializable {
	private static final long serialVersionUID = -2204829307542201308L;

	private Long id;
	private String name;
	private Integer sequence;

	private List<VCoinsGoods> goods;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public List<VCoinsGoods> getGoods() {
		return goods;
	}

	public void setGoods(List<VCoinsGoods> goods) {
		this.goods = goods;
	}
}
