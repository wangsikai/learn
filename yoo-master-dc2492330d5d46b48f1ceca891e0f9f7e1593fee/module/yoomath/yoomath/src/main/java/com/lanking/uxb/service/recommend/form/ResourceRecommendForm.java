package com.lanking.uxb.service.recommend.form;

import com.lanking.cloud.domain.common.resource.question.Question.Type;

/**
 * 资源推荐提交条件
 * 
 * @author wangsenhao
 *
 */
public class ResourceRecommendForm {

	/**
	 * 题目类型
	 */
	private Type questionType;
	/**
	 * 题目难度
	 */
	private DifficultyType difficultyType;
	/**
	 * 知识点code
	 */
	private Long knowledgeCode;

	public Type getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Type questionType) {
		this.questionType = questionType;
	}

	public enum DifficultyType {
		BASIS, IMPROVE, HARD;
	}

	public DifficultyType getDifficultyType() {
		return difficultyType;
	}

	public void setDifficultyType(DifficultyType difficultyType) {
		this.difficultyType = difficultyType;
	}

	public Long getKnowledgeCode() {
		return knowledgeCode;
	}

	public void setKnowledgeCode(Long knowledgeCode) {
		this.knowledgeCode = knowledgeCode;
	}

}
