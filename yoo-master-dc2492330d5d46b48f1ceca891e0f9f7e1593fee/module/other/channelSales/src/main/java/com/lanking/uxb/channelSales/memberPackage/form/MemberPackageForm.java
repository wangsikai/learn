package com.lanking.uxb.channelSales.memberPackage.form;

import java.math.BigDecimal;

import com.lanking.cloud.domain.yoo.member.MemberPackageTag;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 套餐表单
 * 
 * @author zemin.song
 * @version 2016年9月26日
 */
public class MemberPackageForm {
	private Long id;
	private int month;
	private UserType userType;
	private MemberType memberType;
	private BigDecimal originalPrice;
	private BigDecimal presentPrice;
	private BigDecimal discount;
	private MemberPackageTag tag;
	private String customTag;
	private int sequence;
	private Integer extraMonth;
	private Long groupId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public BigDecimal getPresentPrice() {
		return presentPrice;
	}

	public void setPresentPrice(BigDecimal presentPrice) {
		this.presentPrice = presentPrice;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public MemberPackageTag getTag() {
		return tag;
	}

	public void setTag(MemberPackageTag tag) {
		this.tag = tag;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

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

	public String getCustomTag() {
		return customTag;
	}

	public void setCustomTag(String customTag) {
		this.customTag = customTag;
	}

	public Integer getExtraMonth() {
		return extraMonth;
	}

	public void setExtraMonth(Integer extraMonth) {
		this.extraMonth = extraMonth;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}
