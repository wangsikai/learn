package com.lanking.uxb.service.examPaper.value;

import com.lanking.uxb.service.resources.value.VQuestion;

import java.io.Serializable;
import java.util.Date;

/**
 * 试卷题目Value
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public class VExamPaperQuestion implements Serializable {
	private static final long serialVersionUID = -2848272863985511991L;

	private long id;
	private long topicId;
	private VQuestion question;
	private Date createAt;
	private Integer sequence;
	private Integer score;

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

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
}
