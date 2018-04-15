package com.lanking.uxb.channelSales.openmember.form;

import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 为老师开通会员查询form
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
public class UserQueryForm {
	// 当前页
	private int page = 1;
	// 分页大小
	private int size = 20;

	// 所属学校名
	private String schoolName;
	// 所属渠道名
	private String channelName;
	// 阶段代码
	private Integer phaseCode;
	// 用户名
	private String accountName;
	// 真实名
	private String userName;
	// 查询用户类型
	private UserType userType = UserType.TEACHER;
	// 查询用户类型
	private Long schoolId;
	// 会员类型
	private MemberType memberType;
	// 班级id
	private Long classId;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}
}
