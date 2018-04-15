package com.lanking.cloud.domain.common.resource.teachAssist;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 教辅
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist")
public class TeachAssist implements Serializable {

	private static final long serialVersionUID = -7149495792532682417L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 供应商ID
	 */
	@Column(name = "vendor_id")
	private Long vendorId;

	/**
	 * 教辅版本ID
	 */
	@Column(name = "teachassist_version_id")
	private Long teachassistVersionId;

	/**
	 * 删除状态
	 */
	@Column(name = "del_status", precision = 3, nullable = false)
	private Status delStatus = Status.ENABLED;

	/**
	 * 创建人ID
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新人ID
	 */
	@Column(name = "update_id")
	private Long updateId;

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

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public Long getTeachassistVersionId() {
		return teachassistVersionId;
	}

	public void setTeachassistVersionId(Long teachassistVersionId) {
		this.teachassistVersionId = teachassistVersionId;
	}

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
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

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
