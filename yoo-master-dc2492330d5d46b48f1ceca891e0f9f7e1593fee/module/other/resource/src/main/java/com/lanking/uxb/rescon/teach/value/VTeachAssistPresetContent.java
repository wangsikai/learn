package com.lanking.uxb.rescon.teach.value;

import java.io.Serializable;

/**
 * 教辅预置内容 VO
 * 
 * @author wangsenhao
 *
 */
public class VTeachAssistPresetContent implements Serializable {

	private static final long serialVersionUID = 9076396479072552039L;
	// 知识体系code
	private Long knowledgeSystemCode;

	// 学习目标
	private String learningGoals;

	// 解题方法
	private String solvingMethod;

	public Long getKnowledgeSystemCode() {
		return knowledgeSystemCode;
	}

	public void setKnowledgeSystemCode(Long knowledgeSystemCode) {
		this.knowledgeSystemCode = knowledgeSystemCode;
	}

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

}
