package com.lanking.uxb.service.resources.value;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 答案VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月20日
 */
public class VAnswer implements Serializable {

	private static final long serialVersionUID = -6533290749471544324L;

	private long id;
	private long questionId;
	private int sequence;
	private String content;

	// fallibleCount学生人数
	private int fallibleCount;
	// 易错答案
	private String fallibleAnswer;
	// 错误答案信息
	private List<VWrongAnswer> wrongAnswers = Lists.newArrayList();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getFallibleCount() {
		return fallibleCount;
	}

	public void setFallibleCount(int fallibleCount) {
		this.fallibleCount = fallibleCount;
	}

	public String getFallibleAnswer() {
		return fallibleAnswer;
	}

	public void setFallibleAnswer(String fallibleAnswer) {
		this.fallibleAnswer = fallibleAnswer;
	}

	public List<VWrongAnswer> getWrongAnswers() {
		return wrongAnswers;
	}

	public void setWrongAnswers(List<VWrongAnswer> wrongAnswers) {
		this.wrongAnswers = wrongAnswers;
	}

}
