package com.lanking.uxb.zycon.HolidayActivity02.value;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户pk记录VO
 * 
 * @author peng.zhao
 * @version 2018-1-18
 */
public class VHolidayActivity02PKRecord implements Serializable {

	private static final long serialVersionUID = 8050899065151237439L;

	/**
	 * 活动代码
	 */
	private Long activityCode;
	
	/**
	 * 对战时间
	 */
	private Date pkAt;
	
	/**
	 * 选手帐号
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
	 * 正确率
	 */
	private String rightRate;
	
	/**
	 * pk选手帐号
	 */
	private String pkAccountName;
	
	/**
	 * pk选手真实姓名
	 */
	private String pkRealName;
	
	/**
	 * pk选手学校
	 */
	private String pkSchoolName;
	
	/**
	 * pk选手正确率
	 */
	private String pkRightRate;

	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}

	public Date getPkAt() {
		return pkAt;
	}

	public void setPkAt(Date pkAt) {
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

	public String getRightRate() {
		return rightRate;
	}

	public void setRightRate(String rightRate) {
		this.rightRate = rightRate;
	}

	public String getPkAccountName() {
		return pkAccountName;
	}

	public void setPkAccountName(String pkAccountName) {
		this.pkAccountName = pkAccountName;
	}

	public String getPkRealName() {
		return pkRealName;
	}

	public void setPkRealName(String pkRealName) {
		this.pkRealName = pkRealName;
	}

	public String getPkSchoolName() {
		return pkSchoolName;
	}

	public void setPkSchoolName(String pkSchoolName) {
		this.pkSchoolName = pkSchoolName;
	}

	public String getPkRightRate() {
		return pkRightRate;
	}

	public void setPkRightRate(String pkRightRate) {
		this.pkRightRate = pkRightRate;
	}
}
