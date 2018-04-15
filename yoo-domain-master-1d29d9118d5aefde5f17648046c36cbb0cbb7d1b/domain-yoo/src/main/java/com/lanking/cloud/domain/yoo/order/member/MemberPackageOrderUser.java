package com.lanking.cloud.domain.yoo.order.member;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.yoo.member.MemberType;

/**
 * 会员订单对应的用户(渠道销售系统开通会员使用此表)
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "member_package_order_user")
public class MemberPackageOrderUser implements Serializable {

	private static final long serialVersionUID = -4445777186336400528L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 关联会员套餐订单ID
	 */
	@Column(name = "member_package_order_id")
	private long memberPackageOrderId;

	/**
	 * 关联会员套餐订单快照ID
	 */
	@Column(name = "member_package_order_snapshot_id")
	private long memberPackageOrderSnapshotId;

	/**
	 * 会员类型,当member_package_id = 0的时,此字段用来判断动态开通的会员类型(冗余字段)
	 */
	@Column(name = "member_type", precision = 3)
	private MemberType memberType;

	/**
	 * 开通用户的渠道代码
	 */
	@Column(name = "channel_code", precision = 5)
	private int channelCode;

	/**
	 * 被开通用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 截止时间，自定义时间的截止时间
	 */
	@Column(name = "deadline", columnDefinition = "datetime(3)")
	private Date deadline;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getMemberPackageOrderId() {
		return memberPackageOrderId;
	}

	public void setMemberPackageOrderId(long memberPackageOrderId) {
		this.memberPackageOrderId = memberPackageOrderId;
	}

	public long getMemberPackageOrderSnapshotId() {
		return memberPackageOrderSnapshotId;
	}

	public void setMemberPackageOrderSnapshotId(long memberPackageOrderSnapshotId) {
		this.memberPackageOrderSnapshotId = memberPackageOrderSnapshotId;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public int getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(int channelCode) {
		this.channelCode = channelCode;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

}
