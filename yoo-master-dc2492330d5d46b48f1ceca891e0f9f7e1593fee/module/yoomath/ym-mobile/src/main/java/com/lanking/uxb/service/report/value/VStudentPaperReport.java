package com.lanking.uxb.service.report.value;

import java.io.Serializable;

/**
 * 学生纸质报告
 * 
 * @author peng.zhao
 */
public class VStudentPaperReport implements Serializable {

	private static final long serialVersionUID = -3029165798706656331L;

	/**
	 * 学生ID
	 */
	private long studentId;

	/**
	 * 学生名
	 */
	private String userName;

	/**
	 * 账户名
	 */
	private String accountName;

	/**
	 * 开始时间
	 */
	private long startDate;

	/**
	 * 结束时间
	 */
	private long endDate;

	/**
	 * 正确率
	 */
	private Integer rightRate;

	/**
	 * 班级正确率
	 */
	private Integer classRightRate;

	/**
	 * 完成率
	 */
	private Integer completionRate;

	/**
	 * 班级完成率
	 */
	private Integer classCompletionRate;

	/**
	 * 正确率排名
	 */
	private Integer rightRateRank;

	/**
	 * 能力图谱
	 */
	private String sectionMap;

	/**
	 * 点评
	 */
	private String comment;

	/**
	 * 班级名
	 */
	private String clazzName;

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public Integer getRightRate() {
		return rightRate;
	}

	public void setRightRate(Integer rightRate) {
		this.rightRate = rightRate;
	}

	public Integer getClassRightRate() {
		return classRightRate;
	}

	public void setClassRightRate(Integer classRightRate) {
		this.classRightRate = classRightRate;
	}

	public Integer getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(Integer completionRate) {
		this.completionRate = completionRate;
	}

	public Integer getClassCompletionRate() {
		return classCompletionRate;
	}

	public void setClassCompletionRate(Integer classCompletionRate) {
		this.classCompletionRate = classCompletionRate;
	}

	public Integer getRightRateRank() {
		return rightRateRank;
	}

	public void setRightRateRank(Integer rightRateRank) {
		this.rightRateRank = rightRateRank;
	}

	public String getSectionMap() {
		return sectionMap;
	}

	public void setSectionMap(String sectionMap) {
		this.sectionMap = sectionMap;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}
}
