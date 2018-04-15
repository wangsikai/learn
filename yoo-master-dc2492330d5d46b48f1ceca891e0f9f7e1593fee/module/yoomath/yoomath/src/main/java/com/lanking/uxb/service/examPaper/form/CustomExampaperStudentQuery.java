package com.lanking.uxb.service.examPaper.form;

import java.io.Serializable;

/**
 * 学生组卷查询表单.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月12日
 */
public class CustomExampaperStudentQuery implements Serializable {
	private static final long serialVersionUID = -2932629518361423730L;

	private Long studentId;
	private Integer pageSize = 20;
	private Integer page = 1;

	/**
	 * 班级ID.
	 */
	private Long clazzID;

	/**
	 * 组卷开始时间.
	 */
	private Long bt;

	/**
	 * 组卷结束时间.
	 */
	private Long et;

	/**
	 * 关键字.
	 */
	private String key;

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
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

	public Long getClazzID() {
		return clazzID;
	}

	public void setClazzID(Long clazzID) {
		this.clazzID = clazzID;
	}

	public Long getBt() {
		return bt;
	}

	public void setBt(Long bt) {
		this.bt = bt;
	}

	public Long getEt() {
		return et;
	}

	public void setEt(Long et) {
		this.et = et;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
