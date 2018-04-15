package com.lanking.cloud.domain.support.common.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 支撑系统权限-用户&用户组关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@MappedSuperclass
public class ConsoleUserGroupKey implements Serializable {

	private static final long serialVersionUID = -4408732141315565265L;

	/**
	 * 用户ID
	 */
	@Id
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 群组ID
	 */
	@Id
	@Column(name = "group_id")
	private Long groupId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
}
