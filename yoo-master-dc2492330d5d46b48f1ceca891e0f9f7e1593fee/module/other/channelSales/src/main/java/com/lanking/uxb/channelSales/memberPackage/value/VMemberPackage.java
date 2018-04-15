package com.lanking.uxb.channelSales.memberPackage.value;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lanking.cloud.domain.yoo.member.MemberPackageTag;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 套餐信息.
 * 
 * @author zemin.song
 * @version 2016年9月26日
 */
public class VMemberPackage implements Serializable {
	private static final long serialVersionUID = 3541294068434334909L;
	private Long id;
	private int month;
	private UserType userType;
	private MemberType memberType;
	private BigDecimal originalPrice;
	private BigDecimal presentPrice;
	private Long memberPackageGroupId;
	private BigDecimal discount;
	private String customTag;
	private MemberPackageTag tag;
	private int sequence = 0;
	private int extraMonth;

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

	public int getExtraMonth() {
		return extraMonth;
	}

	public void setExtraMonth(int extraMonth) {
		this.extraMonth = extraMonth;
	}

	public Long getMemberPackageGroupId() {
		return memberPackageGroupId;
	}

	public void setMemberPackageGroupId(Long memberPackageGroupId) {
		this.memberPackageGroupId = memberPackageGroupId;
	}
}
