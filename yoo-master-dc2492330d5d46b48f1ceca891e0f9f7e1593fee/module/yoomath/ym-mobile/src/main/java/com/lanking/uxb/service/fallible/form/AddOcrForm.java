package com.lanking.uxb.service.fallible.form;

import java.util.List;

public class AddOcrForm {

	private Long fileId;
	private Long questionId;
	private List<Integer> knowpoints;

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public List<Integer> getKnowpoints() {
		return knowpoints;
	}

	public void setKnowpoints(List<Integer> knowpoints) {
		this.knowpoints = knowpoints;
	}
}
