package com.lanking.uxb.rescon.teach.value;

import java.io.Serializable;

import com.lanking.uxb.rescon.question.value.VQuestion;

public class VTeachAssistPresetContentFallibleDifficultExample implements Serializable {

	private static final long serialVersionUID = 1554696612039462471L;

	// 预置内容易错疑难ID
	private Long teachassistPcFallibleDifficultId;

	// 题目ID
	private Long questionId;

	private VQuestion question;

	// 错解
	private String wrongAnswer;

	// 错因分析
	private String wrongAnalysis;

	// 解题策略
	private String solvingStrategy;

	private Long id;

	public Long getTeachassistPcFallibleDifficultId() {
		return teachassistPcFallibleDifficultId;
	}

	public void setTeachassistPcFallibleDifficultId(Long teachassistPcFallibleDifficultId) {
		this.teachassistPcFallibleDifficultId = teachassistPcFallibleDifficultId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public VQuestion getQuestion() {
		return question;
	}

	public void setQuestion(VQuestion question) {
		this.question = question;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
