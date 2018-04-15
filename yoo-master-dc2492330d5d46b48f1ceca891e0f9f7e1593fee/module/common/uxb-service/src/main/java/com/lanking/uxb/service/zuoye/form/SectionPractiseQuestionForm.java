package com.lanking.uxb.service.zuoye.form;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;

/**
 * 章节练习题目表单
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.1
 */
public class SectionPractiseQuestionForm {
	private long questionId;
	private Map<Long, List<String>> answers;
	private boolean done;
	private HomeworkAnswerResult result;
	private long id;

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public Map<Long, List<String>> getAnswers() {
		return answers;
	}

	public void setAnswers(Map<Long, List<String>> answers) {
		this.answers = answers;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
