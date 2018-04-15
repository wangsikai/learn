package com.lanking.cloud.domain.yoo.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 用户地址表信息基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public class UserAddressBaseInfo implements Serializable {

	private static final long serialVersionUID = -5734404255883409002L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 地区代码
	 */
	@Column(name = "district_code")
	private long districtCode;

	/**
	 * 地址
	 */
	@Column(name = "address", length = 512)
	private String address;

	/**
	 * 联系人
	 */
	@Column(name = "contact", length = 128)
	private String contact;

	/**
	 * 联系电话
	 */
	@Column(name = "contact_phone", length = 32)
	private String contactPhone;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public long getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(long districtCode) {
		this.districtCode = districtCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

}
