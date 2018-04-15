package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 学生作业班级
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月24日
 */
public class VHomeworkStudentClazz implements Serializable {

	private static final long serialVersionUID = 6966736286967248615L;

	private long id;
	private long classId;
	private long studentId;
	private String mark;
	private Date createAt;
	private Date joinAt;
	private Date exitAt;
	private Status status = Status.ENABLED;

	private VStudentHomeworkStat homeworkStat;
	private VHomeworkClazz homeworkClazz;

	// 班级组id
	private VHomeworkClazzGroup group;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getJoinAt() {
		return joinAt;
	}

	public void setJoinAt(Date joinAt) {
		this.joinAt = joinAt;
	}

	public Date getExitAt() {
		return exitAt;
	}

	public void setExitAt(Date exitAt) {
		this.exitAt = exitAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public VStudentHomeworkStat getHomeworkStat() {
		return homeworkStat;
	}

	public void setHomeworkStat(VStudentHomeworkStat homeworkStat) {
		this.homeworkStat = homeworkStat;
	}

	public VHomeworkClazz getHomeworkClazz() {
		return homeworkClazz;
	}

	public void setHomeworkClazz(VHomeworkClazz homeworkClazz) {
		this.homeworkClazz = homeworkClazz;
	}

	public VHomeworkClazzGroup getGroup() {
		return group;
	}

	public void setGroup(VHomeworkClazzGroup group) {
		this.group = group;
	}
}
