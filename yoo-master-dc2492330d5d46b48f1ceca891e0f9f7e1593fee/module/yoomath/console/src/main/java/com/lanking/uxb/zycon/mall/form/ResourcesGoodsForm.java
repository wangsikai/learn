package com.lanking.uxb.zycon.mall.form;

import java.math.BigDecimal;
import java.util.Date;

import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsCategory;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsStatus;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsType;

/**
 * 后台商品价格
 * 
 * @author zemin.song
 */
public class ResourcesGoodsForm {

	private Long id;

	private String name;

	private ResourcesGoodsCategory category;

	private BigDecimal price;

	private BigDecimal priceRMB;

	private String recommendReason;

	private Long resourcesId;

	private ResourcesGoodsStatus status;

	private ResourcesGoodsType type;
	// 上架
	private Date salesTime;
	// 下架
	private Date soldOutTime;

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

	public String getRecommendReason() {
		return recommendReason;
	}

	public void setRecommendReason(String recommendReason) {
		this.recommendReason = recommendReason;
	}

	public ResourcesGoodsStatus getStatus() {
		return status;
	}

	public void setStatus(ResourcesGoodsStatus status) {
		this.status = status;
	}

	public ResourcesGoodsType getType() {
		return type;
	}

	public void setType(ResourcesGoodsType type) {
		this.type = type;
	}

	public Long getResourcesId() {
		return resourcesId;
	}

	public void setResourcesId(Long resourcesId) {
		this.resourcesId = resourcesId;
	}

	public Date getSalesTime() {
		return salesTime;
	}

	public void setSalesTime(Date salesTime) {
		this.salesTime = salesTime;
	}

	public Date getSoldOutTime() {
		return soldOutTime;
	}

	public void setSoldOutTime(Date soldOutTime) {
		this.soldOutTime = soldOutTime;
	}

}
