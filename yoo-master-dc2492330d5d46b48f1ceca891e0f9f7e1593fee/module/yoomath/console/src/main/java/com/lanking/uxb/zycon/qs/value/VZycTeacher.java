package com.lanking.uxb.zycon.qs.value;

import com.lanking.cloud.domain.yoo.user.Sex;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
public class VZycTeacher {
	private String accountName;
	private String mobile;
	private String email;
	private String realName;
	private Sex sex;
	private String schoolName;
	private Integer phaseCode;
	private Integer subjectCode;
	private String phaseName;
	private String subjectName;
	private Long userId;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
}
