package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 考试活动试卷题目
 * 
 * <pre>
 * 2017.12.26期末考试活动
 * </pre>
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年12月26日
 */
public class VExamActivity001Question implements Serializable {

	private static final long serialVersionUID = 1996134198635574311L;

	private Long code;

	private String name;

	private com.lanking.cloud.domain.common.resource.question.Question.Type type;

	private Integer textbookCategoryCode;

	private Integer grade;

	private BigDecimal difficulty;

	private Integer questionCount;

	private boolean done;

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public com.lanking.cloud.domain.common.resource.question.Question.Type getType() {
		return type;
	}

	public void setType(com.lanking.cloud.domain.common.resource.question.Question.Type type) {
		this.type = type;
	}

	public Integer getTextbookCategoryCode() {
		return textbookCategoryCode;
	}

	public void setTextbookCategoryCode(Integer textbookCategoryCode) {
		this.textbookCategoryCode = textbookCategoryCode;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}
	
}
