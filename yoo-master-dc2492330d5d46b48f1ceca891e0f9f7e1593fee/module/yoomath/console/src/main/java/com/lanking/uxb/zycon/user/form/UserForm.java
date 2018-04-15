package com.lanking.uxb.zycon.user.form;

import java.io.Serializable;

import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 保存的提交信息
 * 
 * @author wangsenhao
 *
 */
public class UserForm implements Serializable {

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
	private String schoolName;
	private Long schoolId;
	private String workAt;
	private Integer titleCode;
	private Integer dutyCode;
	private Long userId;
	private Long accountId;
	private String password;
	private Boolean isAdd;
	// 是否需要校验手机号已存在
	private Boolean isCheckMobile;
	// 是否需要校验邮箱号已存在
	private Boolean isCheckEmail;
	// 是否短信提醒
	private Boolean smsNotice;
	private Integer enterYear;

	public Boolean getSmsNotice() {
		return smsNotice;
	}

	public void setSmsNotice(Boolean smsNotice) {
		this.smsNotice = smsNotice;
	}

	public Boolean getIsCheckMobile() {
		return isCheckMobile;
	}

	public void setIsCheckMobile(Boolean isCheckMobile) {
		this.isCheckMobile = isCheckMobile;
	}

	public Boolean getIsCheckEmail() {
		return isCheckEmail;
	}

	public void setIsCheckEmail(Boolean isCheckEmail) {
		this.isCheckEmail = isCheckEmail;
	}

	public Boolean getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(Boolean isAdd) {
		this.isAdd = isAdd;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public String getWorkAt() {
		return workAt;
	}

	public void setWorkAt(String workAt) {
		this.workAt = workAt;
	}

	public Integer getTitleCode() {
		return titleCode;
	}

	public void setTitleCode(Integer titleCode) {
		this.titleCode = titleCode;
	}

	public Integer getDutyCode() {
		return dutyCode;
	}

	public void setDutyCode(Integer dutyCode) {
		this.dutyCode = dutyCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Integer getEnterYear() {
		return enterYear;
	}

	public void setEnterYear(Integer enterYear) {
		this.enterYear = enterYear;
	}

}
