package com.lanking.uxb.zycon.activity.form;

import java.io.Serializable;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationExamTag;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationGrade;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 科举查询表单 <br>
 * zemin.song
 */
public class ZycActivityUserForm implements Serializable {
	private static final long serialVersionUID = 7837585034214098088L;

	private Long activityCode;
	// 账户名
	private String accountName;
	// 真实姓名
	private String realName;
	// 渠道名
	private String channelName;
	// 学校名
	private String schoolName;
	// 考场分类
	private Integer room;
	// 考试分类
	private ImperialExaminationType type;
	// 年级
	private ImperialExaminationGrade grade;
	// 教材分类
	private Integer category;
	// 成绩来源
	private ImperialExaminationExamTag tag;
	// 班级码
	private String clazzCode;
	// 奖项级别
	private Integer awardLevel;
	// 角色  0代表老师，1代表学生
	private Integer role;
	// 冻结状态
	private Status status;
	
	private Integer process;
	//阶段，1 小学  2 初中 3 高中
	private Integer phase;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
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

	public ImperialExaminationGrade getGrade() {
		return grade;
	}

	public void setGrade(ImperialExaminationGrade grade) {
		this.grade = grade;
	}

	public String getClazzCode() {
		return clazzCode;
	}

	public void setClazzCode(String clazzCode) {
		this.clazzCode = clazzCode;
	}

	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}

	public ImperialExaminationType getType() {
		return type;
	}

	public void setType(ImperialExaminationType type) {
		this.type = type;
	}

	public Integer getAwardLevel() {
		return awardLevel;
	}

	public void setAwardLevel(Integer awardLevel) {
		this.awardLevel = awardLevel;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getRoom() {
		return room;
	}

	public void setRoom(Integer room) {
		this.room = room;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public ImperialExaminationExamTag getTag() {
		return tag;
	}

	public void setTag(ImperialExaminationExamTag tag) {
		this.tag = tag;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public Integer getProcess() {
		return process;
	}

	public void setProcess(Integer process) {
		this.process = process;
	}

	public Integer getPhase() {
		return phase;
	}

	public void setPhase(Integer phase) {
		this.phase = phase;
	}
	
}
