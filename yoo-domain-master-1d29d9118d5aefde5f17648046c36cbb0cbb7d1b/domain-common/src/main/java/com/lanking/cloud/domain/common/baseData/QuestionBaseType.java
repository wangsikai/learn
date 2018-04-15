package com.lanking.cloud.domain.common.baseData;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.lanking.cloud.domain.common.resource.question.Question;

/**
 * 题目具体题型(question_type)-题目基本题型(question.type:Question.Type)的对应关系
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 * @see Question.Type
 * @see QuestionType
 */
@Entity
@IdClass(QuestionBaseTypeKey.class)
@Table(name = "question_base_type")
public class QuestionBaseType extends QuestionBaseTypeKey {

	private static final long serialVersionUID = -8131560930683793858L;

}
