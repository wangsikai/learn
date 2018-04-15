package com.lanking.uxb.zycon.nationalDayActivity01.form;

import java.io.Serializable;

public class ZycNationalDayActivity01Form implements Serializable {

	private static final long serialVersionUID = 1585451670605019941L;

	// 学校
	private String schoolName;
	// 账户名
	private String accountName;
	// 真实姓名
	private String realName;
	// 渠道名
	private String channelName;
	// 阶段
	private Integer phase;

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

}
