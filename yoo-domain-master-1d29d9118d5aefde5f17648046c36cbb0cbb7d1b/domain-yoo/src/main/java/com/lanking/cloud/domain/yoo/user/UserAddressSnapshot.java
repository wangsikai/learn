package com.lanking.cloud.domain.yoo.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户地址快照
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "user_address_snapshot")
public class UserAddressSnapshot extends UserAddressBaseInfo {

	private static final long serialVersionUID = 2473811778318672217L;

	/**
	 * 用户地址ID
	 */
	@Column(name = "user_address_id")
	private Long userAddressId;

	public Long getUserAddressId() {
		return userAddressId;
	}

	public void setUserAddressId(Long userAddressId) {
		this.userAddressId = userAddressId;
	}

}
