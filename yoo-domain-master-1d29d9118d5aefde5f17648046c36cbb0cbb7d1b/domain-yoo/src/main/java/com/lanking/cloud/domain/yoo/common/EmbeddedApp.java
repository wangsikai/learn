package com.lanking.cloud.domain.yoo.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.frame.system.YooApp;

/**
 * 内嵌应用(app,web上的入口)
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "embedded_app")
public class EmbeddedApp implements Serializable {

	private static final long serialVersionUID = 6154721614444294236L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 32)
	private String name;

	/**
	 * 图片ID
	 */
	@Column(name = "image_id")
	private Long imageId;

	/**
	 * 跳转资源URL
	 */
	@Column(name = "url", length = 500)
	private String url;

	/**
	 * 序号
	 */
	@Column(name = "sequence", precision = 3)
	private Integer sequence;

	/**
	 * 所属APP,为null时表示是web端
	 * 
	 * @see YooApp
	 * 
	 */
	@Column(name = "app", precision = 3)
	private YooApp app;

	/**
	 * 内嵌应用入口位置
	 * 
	 * @see EmbeddedAppLocation
	 */
	@Column(name = "location", precision = 3, columnDefinition = "tinyint default 0")
	private EmbeddedAppLocation location;

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

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public YooApp getApp() {
		return app;
	}

	public void setApp(YooApp app) {
		this.app = app;
	}

	public EmbeddedAppLocation getLocation() {
		return location;
	}

	public void setLocation(EmbeddedAppLocation location) {
		this.location = location;
	}

}
