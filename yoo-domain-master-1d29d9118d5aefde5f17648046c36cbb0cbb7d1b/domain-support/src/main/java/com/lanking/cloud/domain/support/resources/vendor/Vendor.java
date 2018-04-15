package com.lanking.cloud.domain.support.resources.vendor;

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
 * 供应商
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "vendor")
public class Vendor implements Serializable {
	private static final long serialVersionUID = -1038892206860169934L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long id;

	/**
	 * 代码
	 */
	@Column(name = "code", length = 40)
	private String code;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 备注
	 */
	@Column(name = "notes", length = 100)
	private String notes;

	/**
	 * 邮箱
	 */
	@Column(name = "email", length = 40)
	private String email;

	/**
	 * 传真
	 */
	@Column(name = "fax", length = 40)
	private String fax;

	/**
	 * 地址
	 */
	@Column(name = "address", length = 100)
	private String address;

	/**
	 * 介绍
	 */
	@Column(name = "introduce", length = 500)
	private String introduce;

	/**
	 * 电话
	 */
	@Column(name = "tel", length = 40)
	private String tel;

	/**
	 * 级别
	 */
	@Column(name = "level")
	private int level;

	/**
	 * logo图片ID
	 */
	@Column(name = "logo_id")
	private Long logoId;

	/**
	 * 地区代码
	 */
	@Column(name = "district_code")
	private int districtCode;

	/**
	 * 创建人ID
	 */
	@Column(name = "create_id")
	private long createId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新人ID
	 */
	@Column(name = "update_id")
	private long updateId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 状态
	 */
	@Column(precision = 3, nullable = false)
	private Status status = Status.ENABLED;

	/**
	 * 是否可以登录中央资源库
	 */
	@Column(name = "res_flag")
	private Boolean resFlag = false;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Long getLogoId() {
		return logoId;
	}

	public void setLogoId(Long logoId) {
		this.logoId = logoId;
	}

	public int getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(int districtCode) {
		this.districtCode = districtCode;
	}

	public long getCreateId() {
		return createId;
	}

	public void setCreateId(long createId) {
		this.createId = createId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(long updateId) {
		this.updateId = updateId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Boolean getResFlag() {
		return resFlag;
	}

	public void setResFlag(Boolean resFlag) {
		this.resFlag = resFlag;
	}

}
