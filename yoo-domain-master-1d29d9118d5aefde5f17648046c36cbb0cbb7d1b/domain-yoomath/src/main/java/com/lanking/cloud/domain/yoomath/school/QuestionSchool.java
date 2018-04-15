package com.lanking.cloud.domain.yoomath.school;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 题库学校
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "question_school")
public class QuestionSchool implements Serializable {

	private static final long serialVersionUID = 3345911133230596652L;

	/**
	 * 学校ID
	 */
	@Id
	@Column(name = "school_id")
	private long schoolId;

	/**
	 * 题目数量
	 */
	@Column(name = "question_count")
	private long questionCount;

	/**
	 * 学校可以申请录入题目的数量
	 */
	@Column(name = "record_question_count")
	private Long recordQuestionCount;

	/**
	 * 教师校级会员数量
	 */
	@Column(name = "teacher_svip_count", columnDefinition = "bigint default 0")
	private long teacherSchoolVipCount = 0;

	/**
	 * 原来设置校本图书的时候,需要制定绑定这个学校的老师,此字段为老师的数量
	 */
	@Column(name = "teacher_count")
	private long teacherCount;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

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

	public Long getRecordQuestionCount() {
		return recordQuestionCount;
	}

	public void setRecordQuestionCount(Long recordQuestionCount) {
		this.recordQuestionCount = recordQuestionCount;
	}

	public long getTeacherSchoolVipCount() {
		return teacherSchoolVipCount;
	}

	public void setTeacherSchoolVipCount(long teacherSchoolVipCount) {
		this.teacherSchoolVipCount = teacherSchoolVipCount;
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

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
