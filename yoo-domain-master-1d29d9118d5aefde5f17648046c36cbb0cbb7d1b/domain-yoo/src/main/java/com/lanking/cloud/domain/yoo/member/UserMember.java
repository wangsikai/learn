package com.lanking.cloud.domain.yoo.member;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户会员
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "user_member")
public class UserMember extends UserMemberBaseInfo {

	private static final long serialVersionUID = -5694835168153663390L;

}
