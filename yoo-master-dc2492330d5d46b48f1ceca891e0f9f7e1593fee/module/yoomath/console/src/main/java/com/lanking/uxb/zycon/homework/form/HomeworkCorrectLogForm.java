package com.lanking.uxb.zycon.homework.form;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;

/**
 * 后台批改日志form
 *
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
public class HomeworkCorrectLogForm {
	private long sqId;
	private HomeworkAnswerResult result;

	public HomeworkCorrectLogForm() {
	}

	public HomeworkCorrectLogForm(long sqId, HomeworkAnswerResult result) {
		this.sqId = sqId;
		this.result = result;
	}

	public long getSqId() {
		return sqId;
	}

	public void setSqId(long sqId) {
		this.sqId = sqId;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}
}
