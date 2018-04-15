package com.lanking.uxb.channelSales.openmember.value;

import com.alibaba.fastjson.annotation.JSONField;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.uxb.service.code.value.VPhase;

import java.io.Serializable;
import java.util.Date;

/**
 * 开通会员查询教师或者学生VO
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
public class VCsUser implements Serializable {
	// 用户类型
	private UserType userType;
	// 用户id
	private long id;
	// 真实姓名
	private String name;
	// 账号名
	private String accountName;
	// 阶段
	private VPhase phase;
	// 姓别
	private Sex sex;
	// 渠道名
	private String channelName;
	// 学校名
	private String schoolName;
	private MemberType memberType;
	// 渠道商code : 当是渠道商进行管理的时候，与用户当前的渠道商code进行对比，
	// 若不同则不可以选择
	private Integer channelCode;
	// 会员开始时间
	private Date memberBeginDate;
	// 会员结束时间
	private Date memberEndDate;
	// 学校code
	private Long schoolId;

	@JSONField(serialize = false)
	private long accountId;

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public VPhase getPhase() {
		return phase;
	}

	public void setPhase(VPhase phase) {
		this.phase = phase;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public Integer getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(Integer channelCode) {
		this.channelCode = channelCode;
	}

	public Date getMemberBeginDate() {
		return memberBeginDate;
	}

	public void setMemberBeginDate(Date memberBeginDate) {
		this.memberBeginDate = memberBeginDate;
	}

	public Date getMemberEndDate() {
		return memberEndDate;
	}

	public void setMemberEndDate(Date memberEndDate) {
		this.memberEndDate = memberEndDate;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}
}
