package com.lanking.uxb.zycon.qs.value;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.uxb.zycon.base.value.CQuestion;

import java.util.Date;

/**
 * Value for SchoolQuestion
 *
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
public class VZycSchoolQuestion {
	private Long id;
	private Long schoolId;
	private Long questionId;
	private Question.Type type;
	private Integer typeCode;
	private Double difficulty;
	private Integer subjectCode;
	private Date createAt;
	private CQuestion question;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Question.Type getType() {
		return type;
	}

	public void setType(Question.Type type) {
		this.type = type;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public CQuestion getQuestion() {
		return question;
	}

	public void setQuestion(CQuestion question) {
		this.question = question;
	}
}
