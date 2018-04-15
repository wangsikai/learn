package com.lanking.cloud.domain.yoo.order.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员套餐订单表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "member_package_order")
public class MemberPackageOrder extends MemberPackageOrderBaseInfo {

	private static final long serialVersionUID = -2089691179555891519L;

	/**
	 * 关联的快照ID
	 */
	@Column(name = "member_package_order_snapshot_id")
	private long memberPackageOrderSnapshotId;

	public long getMemberPackageOrderSnapshotId() {
		return memberPackageOrderSnapshotId;
	}

	public void setMemberPackageOrderSnapshotId(long memberPackageOrderSnapshotId) {
		this.memberPackageOrderSnapshotId = memberPackageOrderSnapshotId;
	}

}
