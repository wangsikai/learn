package com.lanking.uxb.service.correct.vo;

import java.io.Serializable;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;

/**
 * 批改结果.
 * 
 * @author wanlong.che
 *
 */
public class AnswerCorrectResult implements Serializable {
	private static final long serialVersionUID = -1821113764134207518L;

	public AnswerCorrectResult() {
	};

	public AnswerCorrectResult(long studentHomeworkAnswerId, HomeworkAnswerResult result,
			HomeworkAnswerResult autoResult) {
		this.studentHomeworkAnswerId = studentHomeworkAnswerId;
		this.result = result;
		this.autoResult = autoResult;
		if (result == null) {
			this.result = autoResult;
		}
	}

	/**
	 * 习题答案ID.
	 */
	private long studentHomeworkAnswerId;

	/**
	 * 最终批改结果
	 */
	private HomeworkAnswerResult result;

	/**
	 * 自动批改结果
	 */
	private HomeworkAnswerResult autoResult;

	public long getStudentHomeworkAnswerId() {
		return studentHomeworkAnswerId;
	}

	public void setStudentHomeworkAnswerId(long studentHomeworkAnswerId) {
		this.studentHomeworkAnswerId = studentHomeworkAnswerId;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public HomeworkAnswerResult getAutoResult() {
		return autoResult;
	}

	public void setAutoResult(HomeworkAnswerResult autoResult) {
		this.autoResult = autoResult;
	}

}
