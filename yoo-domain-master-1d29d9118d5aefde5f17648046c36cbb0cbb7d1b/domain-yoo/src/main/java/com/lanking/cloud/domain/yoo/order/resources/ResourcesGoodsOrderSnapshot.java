package com.lanking.cloud.domain.yoo.order.resources;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 资源商品订单快照
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "resources_goods_order_snapshot")
public class ResourcesGoodsOrderSnapshot extends ResourcesGoodsOrderBaseInfo {
	private static final long serialVersionUID = 967471362031326300L;

	/**
	 * 关联资源商品订单ID
	 */
	@Column(name = "resources_goods_order_id")
	private long resourcesGoodsOrderId;

	public long getResourcesGoodsOrderId() {
		return resourcesGoodsOrderId;
	}

	public void setResourcesGoodsOrderId(long resourcesGoodsOrderId) {
		this.resourcesGoodsOrderId = resourcesGoodsOrderId;
	}

}
