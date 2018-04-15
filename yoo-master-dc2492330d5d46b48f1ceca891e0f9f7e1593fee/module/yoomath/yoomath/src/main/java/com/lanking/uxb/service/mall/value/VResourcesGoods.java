package com.lanking.uxb.service.mall.value;

import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsCategory;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsStatus;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsType;

/**
 * 资源商品vo
 * 
 * @author zemin.song
 * @version 2016年9月2日
 */
public class VResourcesGoods extends VGoods {
	private static final long serialVersionUID = -8308291609586253341L;
	private long resourcesId;
	private ResourcesGoodsType resourcesType;
	private ResourcesGoodsCategory category;
	private ResourcesGoodsStatus resourcesGoodsStatus;
	private String reason;

	public ResourcesGoodsCategory getCategory() {
		return category;
	}

	public void setCategory(ResourcesGoodsCategory category) {
		this.category = category;
	}

	public long getResourcesId() {
		return resourcesId;
	}

	public void setResourcesId(long resourcesId) {
		this.resourcesId = resourcesId;
	}

	public ResourcesGoodsStatus getResourcesGoodsStatus() {
		return resourcesGoodsStatus;
	}

	public void setResourcesGoodsStatus(ResourcesGoodsStatus resourcesGoodsStatus) {
		this.resourcesGoodsStatus = resourcesGoodsStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public ResourcesGoodsType getResourcesType() {
		return resourcesType;
	}

	public void setResourcesType(ResourcesGoodsType resourcesType) {
		this.resourcesType = resourcesType;
	}

}
