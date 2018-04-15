package com.lanking.uxb.rescon.teach.value;

import com.lanking.uxb.rescon.question.value.VQuestion;

import java.io.Serializable;

/**
 * @see com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementTopicContent
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementTopicContent implements Serializable {
	private static final long serialVersionUID = -7346170813563056022L;

	private long id;
	private String name;
	private String content;
	private VQuestion question1;
	private String question1Strategy;
	private VQuestion question2;
	private String question2Strategy;
	private VQuestion question3;
	private String question3Strategy;
	private long topicId;
	private Integer sequence;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public VQuestion getQuestion1() {
		return question1;
	}

	public void setQuestion1(VQuestion question1) {
		this.question1 = question1;
	}

	public String getQuestion1Strategy() {
		return question1Strategy;
	}

	public void setQuestion1Strategy(String question1Strategy) {
		this.question1Strategy = question1Strategy;
	}

	public VQuestion getQuestion2() {
		return question2;
	}

	public void setQuestion2(VQuestion question2) {
		this.question2 = question2;
	}

	public String getQuestion2Strategy() {
		return question2Strategy;
	}

	public void setQuestion2Strategy(String question2Strategy) {
		this.question2Strategy = question2Strategy;
	}

	public VQuestion getQuestion3() {
		return question3;
	}

	public void setQuestion3(VQuestion question3) {
		this.question3 = question3;
	}

	public String getQuestion3Strategy() {
		return question3Strategy;
	}

	public void setQuestion3Strategy(String question3Strategy) {
		this.question3Strategy = question3Strategy;
	}

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
