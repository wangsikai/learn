package com.lanking.cloud.domain.support.common.auth;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 支撑系统权限-角色&用户关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@IdClass(ConsoleRoleGroupKey.class)
@Table(name = "con_role_group")
public class ConsoleRoleGroup extends ConsoleRoleGroupKey {

	private static final long serialVersionUID = -7239830718157341746L;
}
