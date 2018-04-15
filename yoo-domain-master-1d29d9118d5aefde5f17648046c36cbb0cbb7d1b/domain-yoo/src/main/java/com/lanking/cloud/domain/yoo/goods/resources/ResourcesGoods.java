package com.lanking.cloud.domain.yoo.goods.resources;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.domain.yoo.goods.Goods;

/**
 * 资源商品
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "resources_goods")
public class ResourcesGoods extends ResourcesGoodsBaseInfo {

	private static final long serialVersionUID = 3300829169981958537L;

	/**
	 * 同goods的ID {@link Goods}.id
	 */
	@Id
	private Long id;

	/**
	 * 关联最新快照ID {@link ResourcesGoodsSnapshot}.id
	 */
	@Column(name = "resources_goods_snapshot_id")
	private long resourcesGoodsSnapshotId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getResourcesGoodsSnapshotId() {
		return resourcesGoodsSnapshotId;
	}

	public void setResourcesGoodsSnapshotId(long resourcesGoodsSnapshotId) {
		this.resourcesGoodsSnapshotId = resourcesGoodsSnapshotId;
	}

}
