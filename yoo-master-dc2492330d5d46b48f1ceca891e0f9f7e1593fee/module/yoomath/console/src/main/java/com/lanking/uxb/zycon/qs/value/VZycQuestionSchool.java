package com.lanking.uxb.zycon.qs.value;

import com.lanking.cloud.sdk.bean.Status;

/**
 * @see com.lanking.cloud.domain.yoomath.school.QuestionSchool
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
public class VZycQuestionSchool {
	private long schoolId;
	private long questionCount;
	private long teacherCount;
	private Status status;
	private long bookCount;
	private long recordQuestionCount;
	private long teacherSchoolVipCount;

	private String schoolName;

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public long getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(long questionCount) {
		this.questionCount = questionCount;
	}

	public long getTeacherCount() {
		return teacherCount;
	}

	public void setTeacherCount(long teacherCount) {
		this.teacherCount = teacherCount;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public long getBookCount() {
		return bookCount;
	}

	public void setBookCount(long bookCount) {
		this.bookCount = bookCount;
	}

	public long getRecordQuestionCount() {
		return recordQuestionCount;
	}

	public void setRecordQuestionCount(long recordQuestionCount) {
		this.recordQuestionCount = recordQuestionCount;
	}

	public long getTeacherSchoolVipCount() {
		return teacherSchoolVipCount;
	}

	public void setTeacherSchoolVipCount(long teacherSchoolVipCount) {
		this.teacherSchoolVipCount = teacherSchoolVipCount;
	}

}
