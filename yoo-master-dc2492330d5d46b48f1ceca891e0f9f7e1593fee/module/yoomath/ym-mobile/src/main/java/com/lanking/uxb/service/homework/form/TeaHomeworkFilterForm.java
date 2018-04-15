package com.lanking.uxb.service.homework.form;

import com.lanking.cloud.domain.type.HomeworkStatus;

/**
 * 教师作业管理过滤条件form
 * 
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @since 2017-7-6
 */
public class TeaHomeworkFilterForm {

	private HomeworkStatus status;

	private Long classId;

	private Long endTime;

	private Integer pageNo;

	private Integer size = 20;

	/**
	 * 用于区分我的班级--作业记录<br>
	 * 新增原因：目前作业管理和我的班级里面的作业记录是用的同一个接口。新增的班级转让需求要求作业管理页看不到已下发的转让作业，
	 * 在班级管理的作业记录可以看到，所以这里用于区分。
	 * 
	 * @author wangsenhao 2017.8.21
	 * 
	 */
	private Boolean classManage = false;

	public HomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(HomeworkStatus status) {
		this.status = status;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Boolean isClassManage() {
		return classManage;
	}

	public void setClassManage(Boolean classManage) {
		this.classManage = classManage;
	}

}
