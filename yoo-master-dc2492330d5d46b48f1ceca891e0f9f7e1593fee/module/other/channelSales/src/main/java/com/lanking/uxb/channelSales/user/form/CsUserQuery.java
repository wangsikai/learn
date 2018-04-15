package com.lanking.uxb.channelSales.user.form;

import java.io.Serializable;

import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 渠道商--用户管理查询条件
 * 
 * @author wangsenhao
 *
 */
public class CsUserQuery implements Serializable {

	private static final long serialVersionUID = 8924464017194773603L;
	private UserType userType;
	private Integer phaseCode;
	private String accountName;
	private String name;
	private String schoolName;
	private MemberType memberType;
	// 班级id,我的渠道--班级成员使用
	private Long classId;
	// 当前用户对应的渠道code
	private Integer channelCode;
	private Integer pageSize = 20;
	private Integer page = 1;
	// 2017.5.4 新增账户状态
	private Status activationStatus;

	// 2018-1-22 添加会员管理
	private String clazzName;

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Integer getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(Integer channelCode) {
		this.channelCode = channelCode;
	}

	public Status getActivationStatus() {
		return activationStatus;
	}

	public void setActivationStatus(Status activationStatus) {
		this.activationStatus = activationStatus;
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

}
