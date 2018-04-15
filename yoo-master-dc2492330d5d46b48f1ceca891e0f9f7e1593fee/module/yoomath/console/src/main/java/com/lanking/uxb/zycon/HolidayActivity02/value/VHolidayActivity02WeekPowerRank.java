package com.lanking.uxb.zycon.HolidayActivity02.value;

import java.io.Serializable;

/**
 * 用户周排名
 * 
 * @author peng.zhao
 * @version 2018-1-25
 */
public class VHolidayActivity02WeekPowerRank implements Serializable {

	private static final long serialVersionUID = -860804475859869020L;

	private Long id;
	private String accountName;
	private String mobile;
	private Integer rank;
	private Integer enterYear;
	private Integer memberType;
	private String channelName;
	private String schoolName;
	private String realName;
	private Integer weekPower;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Integer getEnterYear() {
		return enterYear;
	}
	public void setEnterYear(Integer enterYear) {
		this.enterYear = enterYear;
	}
	public Integer getMemberType() {
		return memberType;
	}
	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Integer getWeekPower() {
		return weekPower;
	}
	public void setWeekPower(Integer weekPower) {
		this.weekPower = weekPower;
	}
}
