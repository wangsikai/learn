package com.lanking.uxb.zycon.homework.form;

import java.io.Serializable;

import com.lanking.cloud.domain.yoomath.homework.QuestionAppealStatus;

/**
 * 问题批改申述记录查询表单 <br>
 * qiuxue.jiang
 */
public class QuestionAppealRecordForm implements Serializable {
	private static final long serialVersionUID = 7837585034214098088L;
	//申述开始时间
	private String startTime;
	//申述结束时间
	private String endTime;
	// 账号名
	private String accountName;
	// 学校名
	private String schoolName;
	// 上次批改人
	private String correctName;
	//状态
	private QuestionAppealStatus status;
	
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
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getCorrectName() {
		return correctName;
	}
	public void setCorrectName(String correctName) {
		this.correctName = correctName;
	}
	public QuestionAppealStatus getStatus() {
		return status;
	}
	public void setStatus(QuestionAppealStatus status) {
		this.status = status;
	}
	
}
