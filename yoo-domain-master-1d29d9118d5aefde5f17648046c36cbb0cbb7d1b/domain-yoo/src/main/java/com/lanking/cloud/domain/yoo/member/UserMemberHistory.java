package com.lanking.cloud.domain.yoo.member;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户会员历史记录表
 * 
 * <pre>
 * 当会员续期时并且当前 {@link UserMember}中的会员记录已经过期 的时候，需要将 {@link UserMember}的记录backup到 {@link UserMemberHistory}表中
 * </pre>
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "user_member_history")
public class UserMemberHistory extends UserMemberBaseInfo {

	private static final long serialVersionUID = -7266733862527717665L;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

}
