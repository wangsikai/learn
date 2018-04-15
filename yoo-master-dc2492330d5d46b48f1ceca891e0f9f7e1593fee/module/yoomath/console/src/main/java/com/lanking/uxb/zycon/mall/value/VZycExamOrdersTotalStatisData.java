package com.lanking.uxb.zycon.mall.value;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsCategory;

/**
 * 试卷订单统计数据VO
 *
 * @author zemin.song
 * @since 2.0.7
 */
public class VZycExamOrdersTotalStatisData implements Serializable {
	private static final long serialVersionUID = 8528702454323209681L;
	private Long id;
	// 编号
	private Long paperId;
	// 名称
	private String name;
	// 分类
	private ResourcesGoodsCategory category;

	private BigDecimal price;
	private BigDecimal priceRMB;
	// 订单次数统计
	private Long totalOrders;

	private BigDecimal totalPrice;
	private BigDecimal totalPriceRMB;

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

	public ResourcesGoodsCategory getCategory() {
		return category;
	}

	public void setCategory(ResourcesGoodsCategory category) {
		this.category = category;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPriceRMB() {
		return priceRMB;
	}

	public void setPriceRMB(BigDecimal priceRMB) {
		this.priceRMB = priceRMB;
	}

	public Long getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(Long totalOrders) {
		this.totalOrders = totalOrders;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getTotalPriceRMB() {
		return totalPriceRMB;
	}

	public void setTotalPriceRMB(BigDecimal totalPriceRMB) {
		this.totalPriceRMB = totalPriceRMB;
	}

	public Long getPaperId() {
		return paperId;
	}

	public void setPaperId(Long paperId) {
		this.paperId = paperId;
	}

}
