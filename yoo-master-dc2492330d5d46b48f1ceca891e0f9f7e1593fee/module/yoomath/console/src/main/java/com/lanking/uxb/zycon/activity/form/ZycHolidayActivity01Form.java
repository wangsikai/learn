package com.lanking.uxb.zycon.activity.form;

import java.io.Serializable;

/**
 * 假期活动询表单 <br>
 * zemin.song
 */
public class ZycHolidayActivity01Form implements Serializable {

	private static final long serialVersionUID = -310037059309753483L;

	private Long activityCode;

	private String clazzName;

	private String schoolName;

	private String accountName;

	private String realName;

	private String channelName;

	private Integer phase;

	private String startPeriodTime;

	private String endPeriodTime;

	private boolean isAll = false;

	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
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

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Integer getPhase() {
		return phase;
	}

	public void setPhase(Integer phase) {
		this.phase = phase;
	}

	public String getStartPeriodTime() {
		return startPeriodTime;
	}

	public void setStartPeriodTime(String startPeriodTime) {
		this.startPeriodTime = startPeriodTime;
	}

	public String getEndPeriodTime() {
		return endPeriodTime;
	}

	public void setEndPeriodTime(String endPeriodTime) {
		this.endPeriodTime = endPeriodTime;
	}

	public boolean isAll() {
		return isAll;
	}

	public void setAll(boolean isAll) {
		this.isAll = isAll;
	}

}
