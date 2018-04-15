package com.lanking.uxb.zycon.qs.form;

import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.sdk.bean.Status;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
public class ZycQuestionSchoolUserQueryForm {
	private Boolean cert;
	private Sex sex;
	private Long phaseCode;
	private Long subjectCode;
	private String loginName;
	private String realName;
	private String mobile;
	private String email;

	public Boolean isCert() {
		return cert;
	}

	public void setCert(Boolean cert) {
		this.cert = cert;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Long getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Long phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Long getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Long subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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
}
