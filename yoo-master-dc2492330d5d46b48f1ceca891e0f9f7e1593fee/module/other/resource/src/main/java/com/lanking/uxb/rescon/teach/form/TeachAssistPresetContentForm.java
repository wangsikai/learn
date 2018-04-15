package com.lanking.uxb.rescon.teach.form;

public class TeachAssistPresetContentForm {
	// 学习目标
	private String learningGoals;

	// 解题方法
	private String solvingMethod;

	private Long id;

	private Long userId;

	private Long knowledgeSystemCode;

	public String getLearningGoals() {
		return learningGoals;
	}

	public void setLearningGoals(String learningGoals) {
		this.learningGoals = learningGoals;
	}

	public String getSolvingMethod() {
		return solvingMethod;
	}

	public void setSolvingMethod(String solvingMethod) {
		this.solvingMethod = solvingMethod;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getKnowledgeSystemCode() {
		return knowledgeSystemCode;
	}

	public void setKnowledgeSystemCode(Long knowledgeSystemCode) {
		this.knowledgeSystemCode = knowledgeSystemCode;
	}

}
