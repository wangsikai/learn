package com.lanking.uxb.service.ranking.api;

/**
 * 答题排名查询条件
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月24日
 */
public class DoQuestionRankingQuery {

	// 7,30,365
	private int day;
	// 班级ID
	private Long classId;
	// 学校ID
	private Long schoolId;
	// 阶段 CODE
	private Integer phaseCode;
	// 学生ID
	private Long studentId;

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

}
