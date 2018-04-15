package com.lanking.cloud.domain.support.common.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 支撑系统权限-角色&菜单关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@MappedSuperclass
public class ConsoleRoleMenKey implements Serializable {
	private static final long serialVersionUID = 2025797441329952696L;

	/**
	 * 角色ID
	 */
	@Id
	@Column(name = "role_id")
	private Long roleId;

	/**
	 * 菜单ID
	 */
	@Id
	@Column(name = "menu_id")
	private Long menuId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
}
