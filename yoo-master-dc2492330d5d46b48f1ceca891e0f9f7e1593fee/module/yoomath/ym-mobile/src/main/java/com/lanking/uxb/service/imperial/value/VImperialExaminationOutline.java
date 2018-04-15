package com.lanking.uxb.service.imperial.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.uxb.service.code.value.VKnowledgePoint;

/**
 * 试卷概要VO <br>
 * 未下发和下发情况下都需要显示
 * 
 * @author wangsenhao
 *
 */
public class VImperialExaminationOutline implements Serializable {

	private static final long serialVersionUID = 617439306918450141L;

	/**
	 * 包含知识点
	 */
	private List<VKnowledgePoint> knowledgePoints = Lists.newArrayList();
	/**
	 * 题目数量
	 */
	private long questionCount = 0;
	/**
	 * 难度
	 */
	private BigDecimal difficulty;
	/**
	 * 答题开始时间
	 */
	private Date answerStartAt;
	/**
	 * 答题结束时间
	 */
	private Date answerEndAt;
	/**
	 * 第一个班级的作业Id
	 */
	private Long homeworkId;

	public List<VKnowledgePoint> getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<VKnowledgePoint> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}

	public long getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(long questionCount) {
		this.questionCount = questionCount;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public Date getAnswerStartAt() {
		return answerStartAt;
	}

	public void setAnswerStartAt(Date answerStartAt) {
		this.answerStartAt = answerStartAt;
	}

	public Date getAnswerEndAt() {
		return answerEndAt;
	}

	public void setAnswerEndAt(Date answerEndAt) {
		this.answerEndAt = answerEndAt;
	}

	public Long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(Long homeworkId) {
		this.homeworkId = homeworkId;
	}

}
