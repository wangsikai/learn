package com.lanking.cloud.domain.yoo.order.resources;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsType;
import com.lanking.cloud.domain.yoo.order.GoodsOrderBaseInfo;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;

/**
 * 资源订单信息基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public class ResourcesGoodsOrderBaseInfo extends GoodsOrderBaseInfo {

	private static final long serialVersionUID = -5008161105183500657L;

	/**
	 * 资源类型
	 */
	@Column(name = "type", precision = 3)
	private ResourcesGoodsType type;

	/**
	 * 订单状态
	 */
	@Column(name = "status", precision = 3)
	private GoodsOrderStatus status = GoodsOrderStatus.NOT_PAY;

	/**
	 * 资源商品ID
	 */
	@Column(name = "resources_goods_id")
	private Long resourcesGoodsId;

	/**
	 * 资源商品快照ID
	 */
	@Column(name = "resources_goods_snapshot_id")
	private Long resourcesGoodsSnapshotId;

	/**
	 * 附加数据，一般用来存储跟业务相关的标识数据等
	 */
	@Column(name = "attach_data", length = 4000)
	private String attachData;

	public ResourcesGoodsType getType() {
		return type;
	}

	public void setType(ResourcesGoodsType type) {
		this.type = type;
	}

	public GoodsOrderStatus getStatus() {
		return status;
	}

	public void setStatus(GoodsOrderStatus status) {
		this.status = status;
	}

	public Long getResourcesGoodsId() {
		return resourcesGoodsId;
	}

	public void setResourcesGoodsId(Long resourcesGoodsId) {
		this.resourcesGoodsId = resourcesGoodsId;
	}

	public Long getResourcesGoodsSnapshotId() {
		return resourcesGoodsSnapshotId;
	}

	public void setResourcesGoodsSnapshotId(Long resourcesGoodsSnapshotId) {
		this.resourcesGoodsSnapshotId = resourcesGoodsSnapshotId;
	}

	public String getAttachData() {
		return attachData;
	}

	public void setAttachData(String attachData) {
		this.attachData = attachData;
	}
}
