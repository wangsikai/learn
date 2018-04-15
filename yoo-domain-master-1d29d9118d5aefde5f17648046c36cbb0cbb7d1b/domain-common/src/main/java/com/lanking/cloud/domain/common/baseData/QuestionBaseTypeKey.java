package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;

/**
 * 题目具体题型(question_type)-题目基本题型(question.type:Question.Type)的对应关系
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 * @see Question.Type
 * @see QuestionType
 */
@MappedSuperclass
public class QuestionBaseTypeKey implements Serializable {
	private static final long serialVersionUID = -4462893152574941029L;

	/**
	 * 题目具体题型CODE(question_type.code)
	 * 
	 * @see QuestionType
	 */
	@Id
	@Column(name = "question_code", nullable = false)
	private Integer questionCode;

	/**
	 * 题目基本题型(question.type)
	 * 
	 * @see Question.Type
	 */
	@Id
	@Column(name = "base_code", nullable = false)
	private Type baseCode;

	public QuestionBaseTypeKey(Integer questionCode, Type baseCode) {
		this.questionCode = questionCode;
		this.baseCode = baseCode;
	}

	public QuestionBaseTypeKey() {
	}

	public Integer getQuestionCode() {
		return questionCode;
	}

	public void setQuestionCode(Integer questionCode) {
		this.questionCode = questionCode;
	}

	public Type getBaseCode() {
		return baseCode;
	}

	public void setBaseCode(Type baseCode) {
		this.baseCode = baseCode;
	}
}
