package com.lanking.cloud.domain.base.file;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 文件处理样式
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "file_style")
public class FileStyle implements Serializable {
	private static final long serialVersionUID = 6087497097940152883L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 所适用的空间ID
	 */
	@Column(name = "space_id", nullable = false)
	private long spaceId;

	/**
	 * 名称
	 */
	@Column(name = "name", unique = true)
	private String name;

	/**
	 * 宽度
	 */
	@Column(name = "width", precision = 5, nullable = false)
	private Integer width;

	/**
	 * 高度
	 */
	@Column(name = "height", precision = 5, nullable = false)
	private Integer height;

	/**
	 * 质量
	 */
	@Column(name = "quality", nullable = false)
	private int quality;

	/**
	 * 文件处理模式
	 * 
	 * @see StyleMode
	 */
	@Column(precision = 3, nullable = false)
	private StyleMode mode;

	/**
	 * 状态
	 * 
	 * @see Status
	 */
	@Column(precision = 3, nullable = false)
	private Status status = Status.ENABLED;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(long spaceId) {
		this.spaceId = spaceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public StyleMode getMode() {
		return mode;
	}

	public void setMode(StyleMode mode) {
		this.mode = mode;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
