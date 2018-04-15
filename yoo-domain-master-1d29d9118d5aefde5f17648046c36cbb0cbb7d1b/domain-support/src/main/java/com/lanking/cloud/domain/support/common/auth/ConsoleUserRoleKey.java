package com.lanking.cloud.domain.support.common.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 支撑系统权限-用戶&角色关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@MappedSuperclass
public class ConsoleUserRoleKey implements Serializable {

	private static final long serialVersionUID = 3289361557888642001L;

	/**
	 * 用户ID
	 */
	@Id
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 角色ID
	 */
	@Id
	@Column(name = "role_id")
	private Long roleId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}
