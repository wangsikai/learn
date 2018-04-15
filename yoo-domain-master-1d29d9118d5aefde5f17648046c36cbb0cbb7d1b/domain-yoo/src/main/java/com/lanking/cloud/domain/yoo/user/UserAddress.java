package com.lanking.cloud.domain.yoo.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 用户地址
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "user_address")
public class UserAddress extends UserAddressBaseInfo {

	private static final long serialVersionUID = 4183792272420328025L;

	/**
	 * 用户地址快照ID
	 */
	@Column(name = "user_address_snapshot_id")
	private Long userAddressSnapshotId;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status;

	public Long getUserAddressSnapshotId() {
		return userAddressSnapshotId;
	}

	public void setUserAddressSnapshotId(Long userAddressSnapshotId) {
		this.userAddressSnapshotId = userAddressSnapshotId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
