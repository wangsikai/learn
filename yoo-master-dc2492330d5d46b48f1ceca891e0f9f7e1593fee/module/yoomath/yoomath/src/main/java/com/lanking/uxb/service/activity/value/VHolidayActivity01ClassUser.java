package com.lanking.uxb.service.activity.value;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lanking.cloud.domain.yoo.member.MemberType;

public class VHolidayActivity01ClassUser implements Serializable {

	private static final long serialVersionUID = -6196120358695764989L;

	/**
	 * 用户ID(学生用户)
	 */
	private long userId;

	private String name;

	/**
	 * 用户会员类型
	 */
	private MemberType memberType;

	private Long avatarId;
	private String avatarUrl = "";
	private String minAvatarUrl = "";

	/**
	 * 作业提交率
	 */
	private BigDecimal submitRate;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public Long getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(Long avatarId) {
		this.avatarId = avatarId;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getMinAvatarUrl() {
		return minAvatarUrl;
	}

	public void setMinAvatarUrl(String minAvatarUrl) {
		this.minAvatarUrl = minAvatarUrl;
	}

	public BigDecimal getSubmitRate() {
		return submitRate;
	}

	public void setSubmitRate(BigDecimal submitRate) {
		this.submitRate = submitRate;
	}

}
