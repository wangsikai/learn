package com.lanking.uxb.zycon.HolidayActivity02.form;

import java.io.Serializable;

/**
 * 活动用户查询条件
 * 
 * @author peng.zhao
 * @version 2018-1-16
 */
public class ZycHolidayActivity02UserForm implements Serializable {

	private static final long serialVersionUID = 6342430451293991221L;

	/**
	 * 活动code
	 */
	private Long activityCode;
	
	/**
	 * 报名日期
	 */
	private String createAt;
	
	/**
	 * 账户名
	 */
	private String accountName;
	
	/**
	 * 手机号码
	 */
	private String mobile;
	
	/**
	 * 真实姓名
	 */
	private String realName;
	
	/**
	 * 学校
	 */
	private String schoolName;
	
	/**
	 * 渠道
	 */
	private String channelName;

	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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
}
