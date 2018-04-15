package com.lanking.uxb.service.fallible.form;

import java.util.List;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.uxb.service.base.form.AbstractHomeworkQuestionForm;

/**
 * 错题练习提交参数
 * 
 * @since 2.0.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月1日
 */
public class FallibleExerciseForm extends AbstractHomeworkQuestionForm {

	private String latexAnswers;
	private HomeworkAnswerResult result;
	private String itemResults;
	private List<HomeworkAnswerResult> itemResultList;
	// 学生错题id
	private Long stuFallbileId;

	public String getLatexAnswers() {
		return latexAnswers;
	}

	public void setLatexAnswers(String latexAnswers) {
		this.latexAnswers = latexAnswers;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public String getItemResults() {
		return itemResults;
	}

	public void setItemResults(String itemResults) {
		this.itemResults = itemResults;
	}

	public List<HomeworkAnswerResult> getItemResultList() {
		return itemResultList;
	}

	public void setItemResultList(List<HomeworkAnswerResult> itemResultList) {
		this.itemResultList = itemResultList;
	}

	public Long getStuFallbileId() {
		return stuFallbileId;
	}

	public void setStuFallbileId(Long stuFallbileId) {
		this.stuFallbileId = stuFallbileId;
	}
}
