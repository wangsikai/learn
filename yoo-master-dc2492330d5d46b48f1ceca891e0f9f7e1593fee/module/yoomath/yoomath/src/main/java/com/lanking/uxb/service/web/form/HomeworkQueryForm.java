package com.lanking.uxb.service.web.form;

import java.io.Serializable;

import com.lanking.cloud.domain.type.HomeworkStatus;

/**
 * 作业查询表单.
 * 
 * @author wlche
 * @since v2.0.3(web v2.0)
 *
 */
public class HomeworkQueryForm implements Serializable {
	private static final long serialVersionUID = 7642049895481918404L;

	private Integer type; // 1 普通作业，2 假期作业
	private Long bt; // 开始时间
	private Long et; // 结束时间
	private String key; // 查询关键字
	private Long classId; // 班级ID

	private String sectionName; // 章节名称
	private Boolean isLineQuestion = false; // 是否是在线题库

	private Integer pageNo = 1;
	private Integer size = 10;

	private HomeworkStatus status; // 作业状态

	/**
	 * 学生使用，0: 全部，1：已下发，2：待批改，3：待下发，4：已下发
	 * 
	 * @since web v2.0.3
	 */
	@Deprecated
	private Integer statusIndex; // 作业状态

	/**
	 * 教师使用，0：全部，1：作业中，2：已截至，3：待批改
	 * @since 小优快批 2018-02-09 pengcheng.yu
	 */
	private Integer hkStatus;
	/**
	 * 学生使用，0-待完成，1-已完成
	 * @since 小优快批 2018-02-22 pengcheng.yu
	 */
	private Integer shkStatus;
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public Boolean getIsLineQuestion() {
		return isLineQuestion;
	}

	public void setIsLineQuestion(Boolean isLineQuestion) {
		this.isLineQuestion = isLineQuestion;
	}

	public HomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(HomeworkStatus status) {
		this.status = status;
	}
	@Deprecated
	public Integer getStatusIndex() {
		return statusIndex;
	}
	@Deprecated
	public void setStatusIndex(Integer statusIndex) {
		this.statusIndex = statusIndex;
	}

	public Integer getHkStatus() {
		return hkStatus;
	}

	public void setHkStatus(Integer hkStatus) {
		this.hkStatus = hkStatus;
	}

	public Integer getShkStatus() {
		return shkStatus;
	}

	public void setShkStatus(Integer shkStatus) {
		this.shkStatus = shkStatus;
	}
	
}
