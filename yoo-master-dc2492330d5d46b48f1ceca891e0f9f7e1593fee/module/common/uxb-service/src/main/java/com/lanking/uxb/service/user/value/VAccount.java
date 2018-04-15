package com.lanking.uxb.service.user.value;

import java.io.Serializable;
import java.util.List;

import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.account.PasswordStatus;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 账户VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月9日
 *
 */
public class VAccount implements Serializable {

	private static final long serialVersionUID = -605963851866584244L;

	private long id;
	private String name;
	private String email;
	private String mobile;
	private Status mobileStatus;
	private Status emailStatus;
	private PasswordStatus passwordStatus;
	private Integer strength;
	private Status pqStatus;
	// 有无密码
	private boolean hasPassword = true;
	// 绑定的第三方列表
	private List<CredentialType> credentialTypes;
	// 可修改帐号状态
	private Status nameUpdateStatus;

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

	public Status getMobileStatus() {
		return mobileStatus;
	}

	public void setMobileStatus(Status mobileStatus) {
		this.mobileStatus = mobileStatus;
	}

	public Status getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(Status emailStatus) {
		this.emailStatus = emailStatus;
	}

	public PasswordStatus getPasswordStatus() {
		return passwordStatus;
	}

	public void setPasswordStatus(PasswordStatus passwordStatus) {
		this.passwordStatus = passwordStatus;
	}

	public Integer getStrength() {
		return strength;
	}

	public void setStrength(Integer strength) {
		this.strength = strength;
	}

	public Status getPqStatus() {
		return pqStatus;
	}

	public void setPqStatus(Status pqStatus) {
		this.pqStatus = pqStatus;
	}

	public boolean isHasPassword() {
		return hasPassword;
	}

	public void setHasPassword(boolean hasPassword) {
		this.hasPassword = hasPassword;
	}

	public List<CredentialType> getCredentialTypes() {
		return credentialTypes;
	}

	public void setCredentialTypes(List<CredentialType> credentialTypes) {
		this.credentialTypes = credentialTypes;
	}

	public Status getNameUpdateStatus() {
		return nameUpdateStatus;
	}

	public void setNameUpdateStatus(Status nameUpdateStatus) {
		this.nameUpdateStatus = nameUpdateStatus;
	}

}
