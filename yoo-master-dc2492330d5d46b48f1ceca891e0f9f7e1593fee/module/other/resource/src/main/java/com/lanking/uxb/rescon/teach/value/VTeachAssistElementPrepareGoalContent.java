package com.lanking.uxb.rescon.teach.value;

import com.lanking.uxb.rescon.basedata.value.VKnowledgePoint;
import com.lanking.uxb.rescon.question.value.VQuestion;

import java.io.Serializable;
import java.util.List;

/**
 * @see com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPrepareGoalContent
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementPrepareGoalContent implements Serializable {
	private static final long serialVersionUID = -4126272785180159208L;
	private long id;
	private String name;
	private List<VKnowledgePoint> knowledgePoints;
	private List<VQuestion> previewQuestions;
	private List<Long> previewQuestionIds;
	private List<VQuestion> selfTestQuestions;
	private List<Long> selfTestQuestionIds;
	private Integer sequence;
	private long goalId;

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public long getGoalId() {
		return goalId;
	}

	public void setGoalId(long goalId) {
		this.goalId = goalId;
	}

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

	public List<VKnowledgePoint> getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<VKnowledgePoint> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}

	public List<VQuestion> getPreviewQuestions() {
		return previewQuestions;
	}

	public void setPreviewQuestions(List<VQuestion> previewQuestions) {
		this.previewQuestions = previewQuestions;
	}

	public List<VQuestion> getSelfTestQuestions() {
		return selfTestQuestions;
	}

	public void setSelfTestQuestions(List<VQuestion> selfTestQuestions) {
		this.selfTestQuestions = selfTestQuestions;
	}

	public List<Long> getPreviewQuestionIds() {
		return previewQuestionIds;
	}

	public void setPreviewQuestionIds(List<Long> previewQuestionIds) {
		this.previewQuestionIds = previewQuestionIds;
	}

	public List<Long> getSelfTestQuestionIds() {
		return selfTestQuestionIds;
	}

	public void setSelfTestQuestionIds(List<Long> selfTestQuestionIds) {
		this.selfTestQuestionIds = selfTestQuestionIds;
	}
}
