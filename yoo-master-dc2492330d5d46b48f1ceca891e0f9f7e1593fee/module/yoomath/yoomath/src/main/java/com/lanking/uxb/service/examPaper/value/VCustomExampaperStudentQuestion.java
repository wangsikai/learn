package com.lanking.uxb.service.examPaper.value;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.uxb.service.resources.value.VQuestion;

public class VCustomExampaperStudentQuestion implements Serializable {
	private static final long serialVersionUID = -9140913688463934561L;

	private Long id;
	private Long questionId;
	private HomeworkAnswerResult result;

	// 试卷习题信息
	private BigDecimal difficulty; // 难度系数
	private Integer answerCount; // 单选题1个,填空题为填空的空数,解答题1个
	private Type questionType;
	private Integer score;
	private Integer sequence;

	// 习题信息
	private VQuestion question;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public VQuestion getQuestion() {
		return question;
	}

	public void setQuestion(VQuestion question) {
		this.question = question;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public Integer getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(Integer answerCount) {
		this.answerCount = answerCount;
	}

	public Type getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Type questionType) {
		this.questionType = questionType;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
