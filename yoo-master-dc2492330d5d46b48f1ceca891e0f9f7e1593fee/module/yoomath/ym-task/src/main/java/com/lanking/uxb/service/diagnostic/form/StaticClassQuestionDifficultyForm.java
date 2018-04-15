package com.lanking.uxb.service.diagnostic.form;

import com.lanking.uxb.service.diagnostic.type.QuestionDifficultyType;

import java.io.Serializable;
import java.util.Collection;

/**
 * 难度相关处理form
 *
 * @author xinyu.zhou
 * @since 2.1.0
 */
public class StaticClassQuestionDifficultyForm implements Serializable {
	private static final long serialVersionUID = 6369034045843959897L;

	private Long rightCount;
	private Long doCount;
	private QuestionDifficultyType type;
	private Double difficulty;
	private Collection<Integer> textbookCodes;

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public StaticClassQuestionDifficultyForm() {
	}

	public StaticClassQuestionDifficultyForm(Long rightCount, Long doCount) {
		this.rightCount = rightCount;
		this.doCount = doCount;
	}

	public Long getRightCount() {
		return rightCount;
	}

	public void setRightCount(Long rightCount) {
		this.rightCount = rightCount;
	}

	public Long getDoCount() {
		return doCount;
	}

	public void setDoCount(Long doCount) {
		this.doCount = doCount;
	}

	public QuestionDifficultyType getType() {
		return type;
	}

	public void setType(QuestionDifficultyType type) {
		this.type = type;
	}

	public Collection<Integer> getTextbookCodes() {
		return textbookCodes;
	}

	public void setTextbookCodes(Collection<Integer> textbookCodes) {
		this.textbookCodes = textbookCodes;
	}
}
