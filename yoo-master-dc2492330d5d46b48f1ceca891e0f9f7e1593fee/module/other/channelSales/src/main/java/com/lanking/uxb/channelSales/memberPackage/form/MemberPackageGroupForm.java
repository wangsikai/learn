package com.lanking.uxb.channelSales.memberPackage.form;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.member.MemberPackageGroupType;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 套餐组表单
 * 
 * @author zemin.song
 * @version 2016年9月26日
 */
public class MemberPackageGroupForm {
	private Long id;
	private String name;
	private UserType userType;
	private MemberType memberType;
	private BigDecimal profits1;
	private BigDecimal profits2;
	private MemberPackageGroupType type;
	private Map<String, List<Long>> map;

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

	public Map<String, List<Long>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<Long>> map) {
		this.map = map;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
