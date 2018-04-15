package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.uxb.service.resources.value.VQuestion;

/**
 * DailyPractiseQuestion VO
 *
 * @see com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractiseQuestion
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.0
 */
public class VDailyPractiseQuestion implements Serializable {

	private static final long serialVersionUID = 7563866375595088537L;

	private long id;
	private Date createAt;
	private Date updateAt;
	private HomeworkAnswerResult latestResult;
	private List<String> latestAnswer;
	private boolean done;

	private VQuestion question;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public HomeworkAnswerResult getLatestResult() {
		return latestResult;
	}

	public void setLatestResult(HomeworkAnswerResult latestResult) {
		this.latestResult = latestResult;
	}

	public List<String> getLatestAnswer() {
		return latestAnswer;
	}

	public void setLatestAnswer(List<String> latestAnswer) {
		this.latestAnswer = latestAnswer;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public VQuestion getQuestion() {
		return question;
	}

	public void setQuestion(VQuestion question) {
		this.question = question;
	}
}
