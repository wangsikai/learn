package com.lanking.uxb.rescon.teach.value;

import com.lanking.uxb.rescon.question.value.VQuestion;

import java.io.Serializable;
import java.util.List;

/**
 * @see com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementFalliblePointContent
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementFalliblePointContent implements Serializable {
	private static final long serialVersionUID = -3469479035516993013L;

	private long id;
	private String name;
	private String analysis;
	private VQuestion fallExampleQuestion;
	private String wrongAnswer;
	private String wrongAnalysis;
	private String strategy;
	private List<VQuestion> practiceQuestions;
	private List<Long> practiceQuestionIds;
	private Integer sequence;
	private long fallpointId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public VQuestion getFallExampleQuestion() {
		return fallExampleQuestion;
	}

	public void setFallExampleQuestion(VQuestion fallExampleQuestion) {
		this.fallExampleQuestion = fallExampleQuestion;
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

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public List<VQuestion> getPracticeQuestions() {
		return practiceQuestions;
	}

	public void setPracticeQuestions(List<VQuestion> practiceQuestions) {
		this.practiceQuestions = practiceQuestions;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public long getFallpointId() {
		return fallpointId;
	}

	public void setFallpointId(long fallpointId) {
		this.fallpointId = fallpointId;
	}

	public List<Long> getPracticeQuestionIds() {
		return practiceQuestionIds;
	}

	public void setPracticeQuestionIds(List<Long> practiceQuestionIds) {
		this.practiceQuestionIds = practiceQuestionIds;
	}
}
