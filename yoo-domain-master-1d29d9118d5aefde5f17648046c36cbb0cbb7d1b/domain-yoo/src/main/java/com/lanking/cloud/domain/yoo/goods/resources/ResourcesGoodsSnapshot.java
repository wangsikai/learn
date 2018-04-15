package com.lanking.cloud.domain.yoo.goods.resources;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 商品快照
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "resources_goods_snapshot")
public class ResourcesGoodsSnapshot extends ResourcesGoodsBaseInfo {

	private static final long serialVersionUID = 4367398509464767731L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 关联资源商品ID {@link ResourcesGoods}.id
	 */
	@Column(name = "resources_goods_id")
	private long resourcesGoodsId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getResourcesGoodsId() {
		return resourcesGoodsId;
	}

	public void setResourcesGoodsId(long resourcesGoodsId) {
		this.resourcesGoodsId = resourcesGoodsId;
	}

}
