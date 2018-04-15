package com.lanking.cloud.domain.yoo.order.resources;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 资源商品订单
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "resources_goods_order")
public class ResourcesGoodsOrder extends ResourcesGoodsOrderBaseInfo {
	private static final long serialVersionUID = 4830250406973727487L;

	/**
	 * 资源商品订单快照ID
	 */
	@Column(name = "resources_goods_order_snapshot_id")
	private long resourcesGoodsOrderSnapshotId;

	public long getResourcesGoodsOrderSnapshotId() {
		return resourcesGoodsOrderSnapshotId;
	}

	public void setResourcesGoodsOrderSnapshotId(long resourcesGoodsOrderSnapshotId) {
		this.resourcesGoodsOrderSnapshotId = resourcesGoodsOrderSnapshotId;
	}

}
