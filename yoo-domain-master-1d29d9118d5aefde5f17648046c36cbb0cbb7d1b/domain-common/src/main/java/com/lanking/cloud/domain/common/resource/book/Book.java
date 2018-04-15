package com.lanking.cloud.domain.common.resource.book;

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
 * 书本
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "book")
public class Book implements Serializable {

	private static final long serialVersionUID = -4378467221884404619L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 供应商ID.
	 */
	@Column(name = "vendor_id")
	private Long vendorId;

	/**
	 * 商品ID(如果此书本有商品转换而来)
	 */
	@Column(name = "product_id")
	private Long productId;

	/**
	 * 所属学校ID(目前一本书只能属于一个学校)
	 */
	@Column(name = "school_id")
	private Long schoolId;

	/**
	 * 开放状态
	 * 
	 * @see BookOpenStatus
	 */
	@Column(name = "open_status", precision = 3, columnDefinition = "tinyint default 0")
	private BookOpenStatus openStatus = BookOpenStatus.NOT_OPEN;

	/**
	 * 开放时间
	 */
	@Column(name = "open_at", columnDefinition = "datetime(3)")
	private Date openAt;

	/**
	 * 创建人
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 书本数据状态
	 */
	@Column(precision = 3, nullable = false)
	private Status status = Status.ENABLED;

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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public BookOpenStatus getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(BookOpenStatus openStatus) {
		this.openStatus = openStatus;
	}

	public Date getOpenAt() {
		return openAt;
	}

	public void setOpenAt(Date openAt) {
		this.openAt = openAt;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
