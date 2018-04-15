package com.lanking.cloud.domain.common.baseData;

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
 * 学校
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "school")
public class School implements Serializable {

	private static final long serialVersionUID = -2211478711101116624L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 学校代码
	 */
	@Column(name = "code", length = 40, unique = true)
	private String code;

	/**
	 * 所属区域代码
	 */
	@Column(name = "district_code")
	private Long districtCode;

	/**
	 * 学校名称
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 首字母
	 */
	@Column(name = "acronym", length = 40)
	private String acronym;

	/**
	 * 学校类型
	 */
	@Column(precision = 3)
	private SchoolType type;

	/**
	 * 状态
	 * 
	 * @see SchoolStatus
	 */
	@Column(precision = 3)
	private SchoolStatus status = SchoolStatus.NOT_OPEN;

	/**
	 * 开放时间
	 */
	@Column(name = "open_at", columnDefinition = "datetime(3)")
	private Date openAt;

	/**
	 * 关闭时间
	 */
	@Column(name = "close_at", columnDefinition = "datetime(3)")
	private Date closeAt;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(Long districtCode) {
		this.districtCode = districtCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public SchoolType getType() {
		return type;
	}

	public void setType(SchoolType type) {
		this.type = type;
	}

	public SchoolStatus getStatus() {
		return status;
	}

	public void setStatus(SchoolStatus status) {
		this.status = status;
	}

	public Date getOpenAt() {
		return openAt;
	}

	public void setOpenAt(Date openAt) {
		this.openAt = openAt;
	}

	public Date getCloseAt() {
		return closeAt;
	}

	public void setCloseAt(Date closeAt) {
		this.closeAt = closeAt;
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
