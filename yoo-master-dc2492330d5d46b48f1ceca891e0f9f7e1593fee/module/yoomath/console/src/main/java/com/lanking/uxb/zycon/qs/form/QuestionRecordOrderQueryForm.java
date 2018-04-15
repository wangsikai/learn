package com.lanking.uxb.zycon.qs.form;

import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrderStatus;

/**
 * @author xinyu.zhou
 * @since 2.6.0
 */
public class QuestionRecordOrderQueryForm {
	// 提交时间查询开始
	private String startTime;
	// 提交时间查询结束
	private String endTime;

	// 状态
	private QuestionRecordOrderStatus status;
	// 账号名
	private String accountName;
	// 学校名
	private String schoolName;

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

	public QuestionRecordOrderStatus getStatus() {
		return status;
	}

	public void setStatus(QuestionRecordOrderStatus status) {
		this.status = status;
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
}
