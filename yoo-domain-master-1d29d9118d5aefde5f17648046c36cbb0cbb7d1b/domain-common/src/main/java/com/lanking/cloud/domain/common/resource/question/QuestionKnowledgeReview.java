package com.lanking.cloud.domain.common.resource.question;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 题目-知识点（复习）
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年10月30日
 */
@Entity
@IdClass(QuestionKnowledgeKey.class)
@Table(name = "question_knowledge_review")
public class QuestionKnowledgeReview extends QuestionKnowledgeReviewKey {

	private static final long serialVersionUID = -6128734273900280009L;

}
