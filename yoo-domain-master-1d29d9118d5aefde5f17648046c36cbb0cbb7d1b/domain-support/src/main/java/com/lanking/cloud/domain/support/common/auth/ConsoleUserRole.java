package com.lanking.cloud.domain.support.common.auth;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 支撑系统权限-用戶&角色关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@IdClass(ConsoleUserRoleKey.class)
@Table(name = "con_user_role")
public class ConsoleUserRole extends ConsoleUserRoleKey {

	private static final long serialVersionUID = -8199189828303663016L;
}
