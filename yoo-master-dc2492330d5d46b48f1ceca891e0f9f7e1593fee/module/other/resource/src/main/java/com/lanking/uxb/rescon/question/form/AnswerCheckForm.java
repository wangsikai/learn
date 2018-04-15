package com.lanking.uxb.rescon.question.form;

import java.util.List;

import com.lanking.uxb.rescon.question.value.VAnswer;

public class AnswerCheckForm {
	Long questionId;
	List<VAnswer> answers;
	Boolean checkFlag;

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public List<VAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<VAnswer> answers) {
		this.answers = answers;
	}

	public Boolean getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(Boolean checkFlag) {
		this.checkFlag = checkFlag;
	}

}
