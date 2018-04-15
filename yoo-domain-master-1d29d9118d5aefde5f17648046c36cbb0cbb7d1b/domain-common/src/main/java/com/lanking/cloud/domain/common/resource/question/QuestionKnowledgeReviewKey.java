package com.lanking.cloud.domain.common.resource.question;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class QuestionKnowledgeReviewKey implements Serializable {

	private static final long serialVersionUID = -9169840761337061702L;

	@Id
	@Column(name = "question_id", nullable = false)
	private long questionId;

	@Id
	@Column(name = "knowledge_code", nullable = false)
	private long knowledgeCode;

	public long getQuestionId() {
		return questionId;
	}

	public QuestionKnowledgeReviewKey(long questionId, int knowledgeCode) {
		this.questionId = questionId;
		this.knowledgeCode = knowledgeCode;
	}

	public QuestionKnowledgeReviewKey() {
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public long getKnowledgeCode() {
		return knowledgeCode;
	}

	public void setKnowledgeCode(long knowledgeCode) {
		this.knowledgeCode = knowledgeCode;
	}
}
