package com.lanking.uxb.rescon.account.form;

import java.io.Serializable;

import com.lanking.cloud.domain.support.resources.vendor.VendorUserStatus;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 用户表单.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年8月12日
 */
public class ResconUserForm implements Serializable {
	private static final long serialVersionUID = -738287620683868155L;

	private String realName;

	private String password;

	private UserType userType;

	private VendorUserStatus status;

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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

	public VendorUserStatus getStatus() {
		return status;
	}

	public void setStatus(VendorUserStatus status) {
		this.status = status;
	}
}
