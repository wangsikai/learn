package com.lanking.cloud.domain.common.resource.question;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 题目&知识点关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@IdClass(QuestionKnowledgeKey.class)
@Table(name = "question_knowledge")
public class QuestionKnowledge extends QuestionKnowledgeKey {

	private static final long serialVersionUID = -7151901454420018745L;

}
