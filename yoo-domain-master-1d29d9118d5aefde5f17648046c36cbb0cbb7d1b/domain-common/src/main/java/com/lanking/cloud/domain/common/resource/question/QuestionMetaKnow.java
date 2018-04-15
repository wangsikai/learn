package com.lanking.cloud.domain.common.resource.question;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 题目&元知识点关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@IdClass(QuestionMetaKnowKey.class)
@Table(name = "question_metaknow")
public class QuestionMetaKnow extends QuestionMetaKnowKey {

	private static final long serialVersionUID = 5757560843262245362L;

}
