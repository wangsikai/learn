package com.lanking.cloud.domain.yoo.goods.resources;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 试卷商品信息基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public abstract class ResourcesGoodsBaseInfo implements Serializable {

	private static final long serialVersionUID = -6544949986976730448L;

	/**
	 * 资源商品类型
	 */
	@Column(name = "type", precision = 3)
	private ResourcesGoodsType type;

	/**
	 * 资源ID
	 */
	@Column(name = "resources_id")
	private Long resourcesId;

	/**
	 * 资源商品类别
	 */
	@Column(name = "category", precision = 3)
	private ResourcesGoodsCategory category;

	/**
	 * 试卷商品状态
	 */
	@Column(name = "status", precision = 3)
	private ResourcesGoodsStatus status = ResourcesGoodsStatus.UN_PUBLISH;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 推荐理由
	 */
	@Column(name = "recommend_reason", length = 100)
	private String recommendReason;

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

	public ResourcesGoodsCategory getCategory() {
		return category;
	}

	public void setCategory(ResourcesGoodsCategory category) {
		this.category = category;
	}

	public ResourcesGoodsStatus getStatus() {
		return status;
	}

	public void setStatus(ResourcesGoodsStatus status) {
		this.status = status;
	}

	public String getRecommendReason() {
		return recommendReason;
	}

	public void setRecommendReason(String recommendReason) {
		this.recommendReason = recommendReason;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
