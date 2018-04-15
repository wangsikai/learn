package com.lanking.uxb.service.zuoye.api;

import java.util.Date;
import java.util.Set;

import com.lanking.cloud.domain.type.HomeworkStatus;

/**
 * 作业查询条件
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
public class ZyHomeworkQuery {

	private long teacherId;
	// isCourse为true的时候,为clazz的ID,为false的时候为homeworkclass的ID
	private Long classId;
	private Set<HomeworkStatus> status;
	private Set<HomeworkStatus> holidayHkstatus;
	private boolean isCourse = true;
	private Integer type; // 1 为普通作业， 2为假期作业，空为全部

	private String key; // 查询关键字
	private Date beginTime; // 查询开始时间
	private Date endTime; // 查询结束时间

	private String sectionName; // 章节名称
	private boolean isLineQuestion = false; // 是否是在线题库
	private boolean classManage = false; // 是否是班级管理--作业列表
	private Integer issueKey;
	private Integer homeworkStatus; // 查询作业状态:1-作业中,2-已截止 ,3-待批改

	public long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Set<HomeworkStatus> getStatus() {
		return status;
	}

	public void setStatus(Set<HomeworkStatus> status) {
		this.status = status;
	}

	public boolean isCourse() {
		return isCourse;
	}

	public void setCourse(boolean isCourse) {
		this.isCourse = isCourse;
	}

	public Set<HomeworkStatus> getHolidayHkstatus() {
		return holidayHkstatus;
	}

	public void setHolidayHkstatus(Set<HomeworkStatus> holidayHkstatus) {
		this.holidayHkstatus = holidayHkstatus;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public boolean isLineQuestion() {
		return isLineQuestion;
	}

	public void setLineQuestion(boolean isLineQuestion) {
		this.isLineQuestion = isLineQuestion;
	}

	public boolean isClassManage() {
		return classManage;
	}

	public void setClassManage(boolean classManage) {
		this.classManage = classManage;
	}

	public Integer getIssueKey() {
		return issueKey;
	}

	public void setIssueKey(Integer issueKey) {
		this.issueKey = issueKey;
	}

	public Integer getHomeworkStatus() {
		return homeworkStatus;
	}

	public void setHomeworkStatus(Integer homeworkStatus) {
		this.homeworkStatus = homeworkStatus;
	}

}
