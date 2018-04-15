package com.lanking.uxb.zycon.homework.form;

import java.io.Serializable;

/**
 * 问题批改记录查询表单 <br>
 * qiuxue.jiang
 */
public class QuestionCorrectLogForm implements Serializable {
	private static final long serialVersionUID = 7837585034214098088L;
	// 作业id
	private Long homeworkId;
	// 账号名
	private String accountName;
	// 题目序号
	private Integer sequence;
	// 是否订正题
	private Integer correct;
	public Long getHomeworkId() {
		return homeworkId;
	}
	public void setHomeworkId(Long homeworkId) {
		this.homeworkId = homeworkId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public Integer getCorrect() {
		return correct;
	}
	public void setCorrect(Integer correct) {
		this.correct = correct;
	}
	
}
