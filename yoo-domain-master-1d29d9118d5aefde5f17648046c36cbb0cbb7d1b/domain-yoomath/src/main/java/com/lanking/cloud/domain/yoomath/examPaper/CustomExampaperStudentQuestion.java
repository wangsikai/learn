package com.lanking.cloud.domain.yoomath.examPaper;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;

/**
 * 组卷学生题目
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "custom_exampaper_student_question")
public class CustomExampaperStudentQuestion implements Serializable {

	private static final long serialVersionUID = 5840744872276610837L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 组卷&学生关系ID {@link CustomExampaperStudent}.id
	 */
	@Column(name = "custom_exampaper_student_id")
	private Long customExampaperStudentId;

	/**
	 * 组卷ID {@link CustomExampaper}.id
	 */
	@Column(name = "custom_exampaper_id")
	private Long customExampaperId;

	/**
	 * 组卷题目ID {@link CustomExampaperQuestion}.id
	 */
	@Column(name = "custom_exampaper_question_id")
	private Long customExampaperQuestionId;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private Long questionId;

	/**
	 * 结果
	 */
	@Column(name = "result", precision = 3)
	private HomeworkAnswerResult result;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomExampaperId() {
		return customExampaperId;
	}

	public void setCustomExampaperId(Long customExampaperId) {
		this.customExampaperId = customExampaperId;
	}

	public Long getCustomExampaperQuestionId() {
		return customExampaperQuestionId;
	}

	public void setCustomExampaperQuestionId(Long customExampaperQuestionId) {
		this.customExampaperQuestionId = customExampaperQuestionId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public Long getCustomExampaperStudentId() {
		return customExampaperStudentId;
	}

	public void setCustomExampaperStudentId(Long customExampaperStudentId) {
		this.customExampaperStudentId = customExampaperStudentId;
	}
}
