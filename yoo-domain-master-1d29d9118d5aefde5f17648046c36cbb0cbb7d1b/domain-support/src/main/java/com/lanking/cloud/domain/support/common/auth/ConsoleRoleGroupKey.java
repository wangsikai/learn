package com.lanking.cloud.domain.support.common.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 支撑系统权限-角色&用户关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@MappedSuperclass
public class ConsoleRoleGroupKey implements Serializable {
	private static final long serialVersionUID = -1984206590045506577L;

	/**
	 * 角色ID
	 */
	@Id
	@Column(name = "role_id")
	private Long roleId;

	/**
	 * 用户组ID
	 */
	@Id
	@Column(name = "group_id")
	private Long groupId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
}
