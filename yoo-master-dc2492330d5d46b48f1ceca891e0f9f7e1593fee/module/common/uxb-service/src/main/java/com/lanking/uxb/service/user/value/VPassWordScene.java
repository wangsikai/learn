package com.lanking.uxb.service.user.value;

import java.io.Serializable;
import java.util.List;

/**
 * 忘记密码的场景VO
 * 
 * @since 2.1
 * @author wangsenhao
 * @version 2015年06月15日
 *
 */
public class VPassWordScene implements Serializable {

	private static final long serialVersionUID = 5750647361220031102L;

	/**
	 * 是否有密保问题
	 */
	private Boolean isPwdQuestion = false;
	/**
	 * 是否有邮箱找回方式
	 */
	private Boolean isEmail = false;
	/**
	 * 是否有手机找回方式
	 */
	private Boolean isMobile = false;
	/**
	 * 邮箱号
	 */
	private String email;
	/**
	 * 邮箱缩略显示
	 */
	private String emailShow;
	/**
	 * 手机号码
	 */
	private String mobile;
	/**
	 * 手机缩略显示
	 */
	private String mobileShow;

	public String getEmailShow() {
		return emailShow;
	}

	public void setEmailShow(String emailShow) {
		this.emailShow = emailShow;
	}

	public String getMobileShow() {
		return mobileShow;
	}

	public void setMobileShow(String mobileShow) {
		this.mobileShow = mobileShow;
	}

	/**
	 * 密保问题(当账号既没有手机号码也没有电子邮箱时返回)
	 */
	private List<VAccountPasswordQuestion> pwdQuestionList;

	public Boolean getIsPwdQuestion() {
		return isPwdQuestion;
	}

	public void setIsPwdQuestion(Boolean isPwdQuestion) {
		this.isPwdQuestion = isPwdQuestion;
	}

	public Boolean getIsEmail() {
		return isEmail;
	}

	public void setIsEmail(Boolean isEmail) {
		this.isEmail = isEmail;
	}

	public Boolean getIsMobile() {
		return isMobile;
	}

	public void setIsMobile(Boolean isMobile) {
		this.isMobile = isMobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<VAccountPasswordQuestion> getPwdQuestionList() {
		return pwdQuestionList;
	}

	public void setPwdQuestionList(List<VAccountPasswordQuestion> pwdQuestionList) {
		this.pwdQuestionList = pwdQuestionList;
	}

}
