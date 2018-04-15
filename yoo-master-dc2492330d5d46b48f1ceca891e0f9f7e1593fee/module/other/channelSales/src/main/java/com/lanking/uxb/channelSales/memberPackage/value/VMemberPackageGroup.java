package com.lanking.uxb.channelSales.memberPackage.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.domain.yoo.member.MemberPackageGroupType;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.uxb.channelSales.channel.value.VUserChannel;

/**
 * 套餐信息vo
 * 
 * @author zemin.song
 * @version 2017年3月2日
 */
public class VMemberPackageGroup implements Serializable {

	private static final long serialVersionUID = 5718342107930645472L;
	private Long id;
	private String name;
	private UserType userType;
	private MemberType memberType;
	private BigDecimal profits1;
	private BigDecimal profits2;
	private MemberPackageGroupType type;
	private List<VMemberPackage> vMemberPackages = Lists.newArrayList();
	// 学校对应的全部渠道
	private List<VUserChannel> userChannel;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public BigDecimal getProfits1() {
		return profits1;
	}

	public void setProfits1(BigDecimal profits1) {
		this.profits1 = profits1;
	}

	public BigDecimal getProfits2() {
		return profits2;
	}

	public void setProfits2(BigDecimal profits2) {
		this.profits2 = profits2;
	}

	public MemberPackageGroupType getType() {
		return type;
	}

	public void setType(MemberPackageGroupType type) {
		this.type = type;
	}

	public List<VMemberPackage> getvMemberPackages() {
		return vMemberPackages;
	}

	public void setvMemberPackages(List<VMemberPackage> vMemberPackages) {
		this.vMemberPackages = vMemberPackages;
	}

	public List<VUserChannel> getUserChannel() {
		return userChannel;
	}

	public void setUserChannel(List<VUserChannel> userChannel) {
		this.userChannel = userChannel;
	}

}
