package com.lanking.uxb.zycon.user.value;

import java.io.Serializable;

/**
 * 用户管理导入VO
 * 
 * @author wangsenhao
 *
 */

// 对应EXCEL：0.班级ID 1.班级入学年份 2.班级名称 3.教师名称 4.教师手机号 5.教师密码 6.教师真实姓名
// 7.教师性别 8.阶段 9.学科 10.学生用户名 11.学生密码 12.学生真实姓名 13.学生性别
// 14.教材15.渠道代码 16.学校代码 17.学生入学年份
// 模板中班级id,手机号,密码是数字类型
public class VZycUserImport implements Serializable {

	private static final long serialVersionUID = -860999983018961756L;

	private String classId;
	private String className;
	private String teacherName;
	private String teacherMobile;
	private String teacherPwd;
	private String teacherRealName;
	private String teacherSex;
	private String phase;
	private String subject;
	private String studentName;
	private String studentPwd;
	private String studentRealName;
	private String studentSex;
	private String textbookCode;
	private String channelCode;
	private String schoolId;
	private String enterYear;
	private String classEnterYear;

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherMobile() {
		return teacherMobile;
	}

	public void setTeacherMobile(String teacherMobile) {
		this.teacherMobile = teacherMobile;
	}

	public String getTeacherPwd() {
		return teacherPwd;
	}

	public void setTeacherPwd(String teacherPwd) {
		this.teacherPwd = teacherPwd;
	}

	public String getTeacherRealName() {
		return teacherRealName;
	}

	public void setTeacherRealName(String teacherRealName) {
		this.teacherRealName = teacherRealName;
	}

	public String getTeacherSex() {
		return teacherSex;
	}

	public void setTeacherSex(String teacherSex) {
		this.teacherSex = teacherSex;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentPwd() {
		return studentPwd;
	}

	public void setStudentPwd(String studentPwd) {
		this.studentPwd = studentPwd;
	}

	public String getStudentRealName() {
		return studentRealName;
	}

	public void setStudentRealName(String studentRealName) {
		this.studentRealName = studentRealName;
	}

	public String getStudentSex() {
		return studentSex;
	}

	public void setStudentSex(String studentSex) {
		this.studentSex = studentSex;
	}

	public String getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(String textbookCode) {
		this.textbookCode = textbookCode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getEnterYear() {
		return enterYear;
	}

	public void setEnterYear(String enterYear) {
		this.enterYear = enterYear;
	}

	public String getClassEnterYear() {
		return classEnterYear;
	}

	public void setClassEnterYear(String classEnterYear) {
		this.classEnterYear = classEnterYear;
	}

}
