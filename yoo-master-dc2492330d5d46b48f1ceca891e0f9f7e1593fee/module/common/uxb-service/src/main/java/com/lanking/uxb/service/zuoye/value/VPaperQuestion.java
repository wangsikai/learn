package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.util.List;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.uxb.service.resources.value.VQuestion;

/**
 * 试卷题目
 * 
 * @author wangsenhao
 *
 */
public class VPaperQuestion implements Serializable {

	private static final long serialVersionUID = 5234406317385745054L;

	private long paperId;

	private long questionId;

	private VQuestion question;

	private List<String> latestAnswer;

	private HomeworkAnswerResult latestResult;

	private boolean done = false;

	public long getPaperId() {
		return paperId;
	}

	public void setPaperId(long paperId) {
		this.paperId = paperId;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public VQuestion getQuestion() {
		return question;
	}

	public void setQuestion(VQuestion question) {
		this.question = question;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public List<String> getLatestAnswer() {
		return latestAnswer;
	}

	public void setLatestAnswer(List<String> latestAnswer) {
		this.latestAnswer = latestAnswer;
	}

	public HomeworkAnswerResult getLatestResult() {
		return latestResult;
	}

	public void setLatestResult(HomeworkAnswerResult latestResult) {
		this.latestResult = latestResult;
	}

}
