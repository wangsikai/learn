package com.lanking.uxb.service.examPaper.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.uxb.service.resources.value.VQuestion;

/**
 * 试卷题目Value
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public class VCustomExamPaperQuestion implements Serializable {
	private static final long serialVersionUID = 9060406704223756305L;
	private long id;
	private long topicId;
	private VQuestion question;
	private Date createAt;
	private Integer sequence;
	private Integer score;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	public VQuestion getQuestion() {
		return question;
	}

	public void setQuestion(VQuestion question) {
		this.question = question;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
}
