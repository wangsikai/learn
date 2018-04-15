package com.lanking.uxb.zycon.homeclazz.api;

import java.io.Serializable;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 班级管理查询条件
 * 
 * @author wangsenhao
 *
 */
public class ClazzQuery implements Serializable {

	private static final long serialVersionUID = 8627248363942422433L;

	private Status status;
	private Integer phaseCode;
	private Integer subjectCode;
	private String schoolName;
	private String loginName;
	private String teacherName;
	private String clazzName;
	private String code;
	private Integer pageSize = 20;
	private Integer page = 1;
	private Integer schoolYear;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Integer getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(Integer schoolYear) {
		this.schoolYear = schoolYear;
	}
}
