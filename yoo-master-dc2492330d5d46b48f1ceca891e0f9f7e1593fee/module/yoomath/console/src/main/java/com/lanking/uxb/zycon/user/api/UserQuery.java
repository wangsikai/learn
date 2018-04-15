package com.lanking.uxb.zycon.user.api;

import java.io.Serializable;
import java.util.List;

import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 用户管理查询条件
 * 
 * @author wangsenhao
 *
 */
public class UserQuery implements Serializable {

	private static final long serialVersionUID = 2807393153774831990L;

	private UserType userType;
	private Status status;
	private Sex sex;
	private Integer phaseCode;
	private Integer subjectCode;
	private String accountName;
	private String name;
	private String mobile;
	private String email;
	/**
	 * 可以输入：江苏，南京，玄武区，不可输入江苏南京这种组合
	 */
	private String schoolName;
	private Integer pageSize = 20;
	private Integer page = 1;
	// 渠道代码
	private Integer channelCode;
	// 区域对象 例如:江苏 32%，南京3201%，玄武320102
	private String districtCodeStr;
	// 重复名称的区县code 集合
	private List<Long> districts;
	/**
	 * 用户会员类型
	 * 
	 * @since 2.5.0
	 */
	private MemberType memberType;

	private Status activationStatus;

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
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

	public Integer getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(Integer channelCode) {
		this.channelCode = channelCode;
	}

	public String getDistrictCodeStr() {
		return districtCodeStr;
	}

	public void setDistrictCodeStr(String districtCodeStr) {
		this.districtCodeStr = districtCodeStr;
	}

	public List<Long> getDistricts() {
		return districts;
	}

	public void setDistricts(List<Long> districts) {
		this.districts = districts;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public Status getActivationStatus() {
		return activationStatus;
	}

	public void setActivationStatus(Status activationStatus) {
		this.activationStatus = activationStatus;
	}

}
