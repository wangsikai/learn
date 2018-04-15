package com.lanking.cloud.domain.common.resource.question;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 题目-知识点（同步）
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年10月30日
 */
@Entity
@IdClass(QuestionKnowledgeKey.class)
@Table(name = "question_knowledge_sync")
public class QuestionKnowledgeSync extends QuestionKnowledgeKey {

	private static final long serialVersionUID = 50834218864699655L;

}
