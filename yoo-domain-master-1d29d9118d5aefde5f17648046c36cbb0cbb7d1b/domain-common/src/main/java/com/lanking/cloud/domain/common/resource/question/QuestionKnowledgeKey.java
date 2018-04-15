package com.lanking.cloud.domain.common.resource.question;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 题目&知识点关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@MappedSuperclass
public class QuestionKnowledgeKey implements Serializable {

	private static final long serialVersionUID = -9169840761337061702L;

	/**
	 * 题目ID
	 */
	@Id
	@Column(name = "question_id", nullable = false)
	private long questionId;

	/**
	 * 知识点代码
	 */
	@Id
	@Column(name = "knowledge_code", nullable = false)
	private long knowledgeCode;

	public long getQuestionId() {
		return questionId;
	}

	public QuestionKnowledgeKey(long questionId, int knowledgeCode) {
		this.questionId = questionId;
		this.knowledgeCode = knowledgeCode;
	}

	public QuestionKnowledgeKey() {
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
