package com.lanking.cloud.domain.support.common.auth;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 支撑系统权限-角色&菜单关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@IdClass(ConsoleRoleMenKey.class)
@Table(name = "con_role_menu")
public class ConsoleRoleMenu extends ConsoleRoleMenKey {

	private static final long serialVersionUID = -2468153189502292995L;

}
