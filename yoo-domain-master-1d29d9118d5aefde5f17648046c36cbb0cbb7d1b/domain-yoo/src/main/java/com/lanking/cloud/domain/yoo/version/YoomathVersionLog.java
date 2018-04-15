package com.lanking.cloud.domain.yoo.version;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * yoomath web 版本更新日志
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "yoomath_version_log")
public class YoomathVersionLog implements Serializable {

	private static final long serialVersionUID = 2558071252796911573L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * yoomath web 版本更新日志
	 */
	@Column(name = "version", length = 20)
	private String version;

	/**
	 * 版本描述
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000)
	private List<String> description = Lists.newArrayList();

	/**
	 * 状态
	 * 
	 * <pre>
	 * Status.ENABLED|status = 0 -> 发布
	 * Status.DISABLED|status = 1 -> 未发布
	 * Status.DELETED|status = 2 -> 删除
	 * </pre>
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.DISABLED;

	/**
	 * 发布时间
	 */
	@Column(name = "publish_at", columnDefinition = "datetime(3)")
	private Date publishAt;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getPublishAt() {
		return publishAt;
	}

	public void setPublishAt(Date publishAt) {
		this.publishAt = publishAt;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
