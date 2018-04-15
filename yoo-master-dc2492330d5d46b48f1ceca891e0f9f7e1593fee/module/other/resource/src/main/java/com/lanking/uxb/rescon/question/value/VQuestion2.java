package com.lanking.uxb.rescon.question.value;

import java.util.List;

import com.beust.jcommander.internal.Lists;

public class VQuestion2 extends VQuestion {

	private static final long serialVersionUID = 8739425028439973886L;

	private List<Boolean> answerIsKatexSpecs = Lists.newArrayList();

	private Boolean contentIsKatexSpecs = true;

	private Boolean hintIsKatexSpecs = true;

	private Boolean analysisIsKatexSpecs = true;

	private List<Boolean> choiceIsKatexSpecs = Lists.newArrayList();

	public List<Boolean> getAnswerIsKatexSpecs() {
		return answerIsKatexSpecs;
	}

	public void setAnswerIsKatexSpecs(List<Boolean> answerIsKatexSpecs) {
		this.answerIsKatexSpecs = answerIsKatexSpecs;
	}

	public Boolean getContentIsKatexSpecs() {
		return contentIsKatexSpecs;
	}

	public void setContentIsKatexSpecs(Boolean contentIsKatexSpecs) {
		this.contentIsKatexSpecs = contentIsKatexSpecs;
	}

	public Boolean getHintIsKatexSpecs() {
		return hintIsKatexSpecs;
	}

	public void setHintIsKatexSpecs(Boolean hintIsKatexSpecs) {
		this.hintIsKatexSpecs = hintIsKatexSpecs;
	}

	public Boolean getAnalysisIsKatexSpecs() {
		return analysisIsKatexSpecs;
	}

	public void setAnalysisIsKatexSpecs(Boolean analysisIsKatexSpecs) {
		this.analysisIsKatexSpecs = analysisIsKatexSpecs;
	}

	public List<Boolean> getChoiceIsKatexSpecs() {
		return choiceIsKatexSpecs;
	}

	public void setChoiceIsKatexSpecs(List<Boolean> choiceIsKatexSpecs) {
		this.choiceIsKatexSpecs = choiceIsKatexSpecs;
	}

}
