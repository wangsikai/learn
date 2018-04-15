package com.lanking.uxb.rescon.teach.form;

import java.util.List;

import com.lanking.cloud.domain.common.resource.card.CardStatus;

/**
 * 易错点提交信息
 * 
 * @author wangsenhao
 *
 */
public class TeachAssistPresetContentFallibleDifficultForm {

	private Long id;

	// 预置内容ID
	private Long teachassistPresetcontentId;

	// 名称
	private String name;

	// 易错点辨析
	private String analysis;

	// 针对性训练题目列表
	private List<Long> targetedTrainingQuestions;

	// 目前一个例题只会对应一个题目

	// 对应的例题ID
	private Long exampleId;

	// 题目ID
	private Long questionId;

	// 错解
	private String wrongAnswer;

	// 错因分析
	private String wrongAnalysis;

	// 解题策略
	private String solvingStrategy;

	private Long userId;

	private Long knowledgeSystemCode;

	private CardStatus status;

	public Long getTeachassistPresetcontentId() {
		return teachassistPresetcontentId;
	}

	public void setTeachassistPresetcontentId(Long teachassistPresetcontentId) {
		this.teachassistPresetcontentId = teachassistPresetcontentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public List<Long> getTargetedTrainingQuestions() {
		return targetedTrainingQuestions;
	}

	public void setTargetedTrainingQuestions(List<Long> targetedTrainingQuestions) {
		this.targetedTrainingQuestions = targetedTrainingQuestions;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getWrongAnswer() {
		return wrongAnswer;
	}

	public void setWrongAnswer(String wrongAnswer) {
		this.wrongAnswer = wrongAnswer;
	}

	public String getWrongAnalysis() {
		return wrongAnalysis;
	}

	public void setWrongAnalysis(String wrongAnalysis) {
		this.wrongAnalysis = wrongAnalysis;
	}

	public String getSolvingStrategy() {
		return solvingStrategy;
	}

	public void setSolvingStrategy(String solvingStrategy) {
		this.solvingStrategy = solvingStrategy;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getExampleId() {
		return exampleId;
	}

	public void setExampleId(Long exampleId) {
		this.exampleId = exampleId;
	}

	public Long getKnowledgeSystemCode() {
		return knowledgeSystemCode;
	}

	public void setKnowledgeSystemCode(Long knowledgeSystemCode) {
		this.knowledgeSystemCode = knowledgeSystemCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CardStatus getStatus() {
		return status;
	}

	public void setStatus(CardStatus status) {
		this.status = status;
	}

}
