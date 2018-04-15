package com.lanking.uxb.service.activity.value;

import java.io.Serializable;


public class HolidayActivity02PkUserInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3480354663954747394L;

	/**
	 * 活动代码
	 */
	private long activityCode;

	/**
	 * 用户ID
	 */
	private long userId;
	
	/**
	 * pk对手ID
	 */
	private long pkUserId;

	/**
	 * 请求pk开始时间
	 */
	private Long pkStartTime;
	
	/**
	 * pk超时时间
	 */
	private Long pkOverTime;

	/**
	 * 是否已经被匹配
	 */
	private Boolean match;
	
	/**
	 * 是否是主动匹配
	 */
	private Boolean positive;

	public long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(long activityCode) {
		this.activityCode = activityCode;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getPkUserId() {
		return pkUserId;
	}

	public void setPkUserId(long pkUserId) {
		this.pkUserId = pkUserId;
	}

	public Long getPkStartTime() {
		return pkStartTime;
	}

	public void setPkStartTime(Long pkStartTime) {
		this.pkStartTime = pkStartTime;
	}

	public Long getPkOverTime() {
		return pkOverTime;
	}

	public void setPkOverTime(Long pkOverTime) {
		this.pkOverTime = pkOverTime;
	}

	public Boolean getMatch() {
		return match;
	}

	public void setMatch(Boolean match) {
		this.match = match;
	}

	public Boolean getPositive() {
		return positive;
	}

	public void setPositive(Boolean positive) {
		this.positive = positive;
	}
	
}
