package com.lanking.uxb.service.activity.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.sdk.bean.Status;

public class VLotteryActivity implements Serializable {

	private static final long serialVersionUID = -1315341451720511652L;

	private long code;
	private String name;
	private String introduction;
	private Date createAt;
	private Date startTime;
	private Date endTime;
	private Status status;
	// 至开始时间剩余时间
	private long remainTime;

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public long getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(long remainTime) {
		this.remainTime = remainTime;
	}
}
