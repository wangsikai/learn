package com.lanking.uxb.channelSales.openmember.form;

import java.util.Date;

import com.lanking.cloud.domain.yoo.member.MemberType;

/**
 * @author xinyu.zhou
 * @since 2.5.0
 */
public class UserMemberCreateForm {
	// 用户id
	private long userId;
	// 会员类型
	private MemberType memberType;
	// 开始时间
	private Date startAt;
	// 结束时间
	private Date endAt;
	// 订单id
	private long orderId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public Date getStartAt() {
		return startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
}
