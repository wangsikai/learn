package com.lanking.uxb.service.zuoye.api;

import java.util.Date;
import java.util.Set;

import com.lanking.cloud.domain.type.StudentHomeworkStatus;

/**
 * 学生作业查询条件
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月20日
 */
public class ZyStudentHomeworkQuery {

	private long studentId;
	private Long courseId;
	private Set<StudentHomeworkStatus> status;
	private boolean isCourse = true;
	private Long classId;

	// id,startTime
	private String cursorType;
	// 学生首页的逻辑
	private boolean isMobileIndex = false;

	/**
	 * web v2.0 使用
	 */
	private Integer type; // 1 为普通作业， 2为假期作业，空为全部
	private String key; // 查询关键字
	private Date beginTime; // 查询开始时间
	private Date endTime; // 查询结束时间
	private String sectionName; // 章节名称

	/**
	 * 0: 全部，1：已下发，2：待批改，3：待下发，4：已下发
	 * 
	 * @since web v2.0.3
	 */
	@Deprecated
	private Integer statusIndex; // 作业状态
	/**
	 * 查询作业状态:0-待完成1-已完成
	 * @since 小优快批
	 */
	private Integer homeworkStatus; 
	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Set<StudentHomeworkStatus> getStatus() {
		return status;
	}

	public void setStatus(Set<StudentHomeworkStatus> status) {
		this.status = status;
	}

	public boolean isCourse() {
		return isCourse;
	}

	public void setCourse(boolean isCourse) {
		this.isCourse = isCourse;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public String getCursorType() {
		return cursorType;
	}

	public void setCursorType(String cursorType) {
		this.cursorType = cursorType;
	}

	public boolean isMobileIndex() {
		return isMobileIndex;
	}

	public void setMobileIndex(boolean isMobileIndex) {
		this.isMobileIndex = isMobileIndex;
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
	@Deprecated
	public Integer getStatusIndex() {
		return statusIndex;
	}
	@Deprecated
	public void setStatusIndex(Integer statusIndex) {
		this.statusIndex = statusIndex;
	}

	public Integer getHomeworkStatus() {
		return homeworkStatus;
	}

	public void setHomeworkStatus(Integer homeworkStatus) {
		this.homeworkStatus = homeworkStatus;
	}
	
}
