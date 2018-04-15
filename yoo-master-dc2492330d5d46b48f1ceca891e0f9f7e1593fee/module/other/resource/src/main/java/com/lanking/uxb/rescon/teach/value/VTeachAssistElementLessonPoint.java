package com.lanking.uxb.rescon.teach.value;

import com.lanking.uxb.rescon.basedata.value.VKnowledgePoint;
import com.lanking.uxb.rescon.question.value.VQuestion;

import java.io.Serializable;
import java.util.List;

/**
 * @see com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementLessonPoint
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementLessonPoint implements Serializable {
	private static final long serialVersionUID = -568314963040719642L;

	private long id;
	private String name;
	private long lessonId;
	private List<VKnowledgePoint> knowledgePoints;
	private List<VQuestion> exampleQuestions;
	private List<Long> exampleQuestionIds;
	private List<VQuestion> expandQuestions;
	private List<Long> expandQuestionIds;
	private int sequence;

	public List<Long> getExampleQuestionIds() {
		return exampleQuestionIds;
	}

	public void setExampleQuestionIds(List<Long> exampleQuestionIds) {
		this.exampleQuestionIds = exampleQuestionIds;
	}

	public List<Long> getExpandQuestionIds() {
		return expandQuestionIds;
	}

	public void setExpandQuestionIds(List<Long> expandQuestionIds) {
		this.expandQuestionIds = expandQuestionIds;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
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

	public long getLessonId() {
		return lessonId;
	}

	public void setLessonId(long lessonId) {
		this.lessonId = lessonId;
	}

	public List<VKnowledgePoint> getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<VKnowledgePoint> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}

	public List<VQuestion> getExampleQuestions() {
		return exampleQuestions;
	}

	public void setExampleQuestions(List<VQuestion> exampleQuestions) {
		this.exampleQuestions = exampleQuestions;
	}

	public List<VQuestion> getExpandQuestions() {
		return expandQuestions;
	}

	public void setExpandQuestions(List<VQuestion> expandQuestions) {
		this.expandQuestions = expandQuestions;
	}
}
