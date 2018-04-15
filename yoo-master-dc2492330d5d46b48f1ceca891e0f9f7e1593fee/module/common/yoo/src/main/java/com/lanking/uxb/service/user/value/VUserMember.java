package com.lanking.uxb.service.user.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.yoo.member.MemberType;

public class VUserMember implements Serializable {

	private static final long serialVersionUID = -8752414615043985023L;

	private long id;
	private MemberType memberType;
	private Date startAt;
	private Date endAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

}
