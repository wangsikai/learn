package com.lanking.cloud.domain.yoo.goods.resources;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 资源商品收藏
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "resources_goods_favorite")
public class ResourcesGoodsFavorite implements Serializable {

	private static final long serialVersionUID = 4607904631798104550L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 资源商品ID {@link ResourcesGoods}.id
	 */
	@Column(name = "resources_goods_id")
	private Long resourcesGoodsId;

	/**
	 * 资源类型
	 */
	@Column(name = "type", precision = 3)
	private ResourcesGoodsType type;

	/**
	 * 资源ID
	 */
	@Column(name = "resources_id")
	private Long resourcesId;

	/**
	 * 创建人ID-收藏人ID
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 创建时间-收藏时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getResourcesGoodsId() {
		return resourcesGoodsId;
	}

	public void setResourcesGoodsId(Long resourcesGoodsId) {
		this.resourcesGoodsId = resourcesGoodsId;
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

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
