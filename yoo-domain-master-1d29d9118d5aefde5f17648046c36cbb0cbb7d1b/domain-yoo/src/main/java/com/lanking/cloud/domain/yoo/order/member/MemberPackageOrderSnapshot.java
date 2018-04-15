package com.lanking.cloud.domain.yoo.order.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员套餐订单快照
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "member_package_order_snapshot")
public class MemberPackageOrderSnapshot extends MemberPackageOrderBaseInfo {

	private static final long serialVersionUID = 3813696447758551825L;

	/**
	 * 关联的订单ID
	 */
	@Column(name = "member_package_order_id")
	private long memberPackageOrderId;

	public long getMemberPackageOrderId() {
		return memberPackageOrderId;
	}

	public void setMemberPackageOrderId(long memberPackageOrderId) {
		this.memberPackageOrderId = memberPackageOrderId;
	}
}
