package com.lanking.uxb.zycon.operation.api;

import java.io.Serializable;

import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 客服对话查询条件
 * 
 * @author wangsenhao
 *
 */
public class TalkQuery implements Serializable {

	private static final long serialVersionUID = -1140015877684546467L;

	private String startTime;
	private String endTime;
	private UserType userType;
	private Integer phaseCode;
	private String keyStr;
	private long userId;
	private Integer pageSize = 40;
	private Integer page = 1;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public String getKeyStr() {
		return keyStr;
	}

	public void setKeyStr(String keyStr) {
		this.keyStr = keyStr;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
