package com.lanking.uxb.rescon.teach.value;

import com.lanking.uxb.rescon.question.value.VQuestion;

import java.io.Serializable;
import java.util.List;

/**
 * @see com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPracticeContent
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementPracticeContent implements Serializable {
	private static final long serialVersionUID = -1082889008118684747L;

	private long id;
	private String name;
	private List<VQuestion> questions;
	private List<Long> questionIds;
	private int sequence;
	private long practiceId;

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

	public List<VQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(List<VQuestion> questions) {
		this.questions = questions;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public long getPracticeId() {
		return practiceId;
	}

	public void setPracticeId(long practiceId) {
		this.practiceId = practiceId;
	}

	public List<Long> getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(List<Long> questionIds) {
		this.questionIds = questionIds;
	}
}
