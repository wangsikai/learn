package com.lanking.uxb.zycon.HolidayActivity02.form;

import java.io.Serializable;

/**
 * 对战记录查询条件
 * 
 * @author peng.zhao
 * @version 2018-1-17
 */
public class ZycHolidayActivity02PKRecordForm implements Serializable {

	private static final long serialVersionUID = -4063048834462187503L;

	/**
	 * 活动code
	 */
	private Long activityCode;
	
	/**
	 * 对战日期
	 */
	private String pkAt;
	
	/**
	 * 账户名
	 */
	private String accountName;
	
	/**
	 * 真实姓名
	 */
	private String realName;
	
	/**
	 * 学校
	 */
	private String schoolName;
	
	/**
	 * 是否真人
	 * 0:机器人,1:真人
	 */
	private Integer realMan;

	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}

	public String getPkAt() {
		return pkAt;
	}

	public void setPkAt(String pkAt) {
		this.pkAt = pkAt;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
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

	public Integer getRealMan() {
		return realMan;
	}

	public void setRealMan(Integer realMan) {
		this.realMan = realMan;
	}
}
