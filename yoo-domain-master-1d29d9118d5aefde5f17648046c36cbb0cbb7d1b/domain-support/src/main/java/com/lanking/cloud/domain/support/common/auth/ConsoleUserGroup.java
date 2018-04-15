package com.lanking.cloud.domain.support.common.auth;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 支撑系统权限-用户&用户组关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@IdClass(ConsoleUserGroupKey.class)
@Table(name = "con_user_group")
public class ConsoleUserGroup extends ConsoleUserGroupKey {

	private static final long serialVersionUID = 4070884205020348799L;
}
