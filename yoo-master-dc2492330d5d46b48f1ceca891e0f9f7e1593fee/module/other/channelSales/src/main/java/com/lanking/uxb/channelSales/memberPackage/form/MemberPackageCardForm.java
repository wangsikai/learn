package com.lanking.uxb.channelSales.memberPackage.form;

import java.math.BigDecimal;

import com.lanking.cloud.domain.yoo.member.MemberPackageCardStatus;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 会员兑换卡创建form
 * 
 * @author zemin.song
 * @version 2016年9月26日
 */
public class MemberPackageCardForm {

	private String code;

	private UserType userType;

	private MemberType memberType;

	private Integer month;

	private BigDecimal price;

	private Long createAt;

	private Long endDate;

	private Integer count;

	private String memo;

	private Long createId;

	private Long updateId;

	private MemberPackageCardStatus status;

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public MemberPackageCardStatus getStatus() {
		return status;
	}

	public void setStatus(MemberPackageCardStatus status) {
		this.status = status;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

}
