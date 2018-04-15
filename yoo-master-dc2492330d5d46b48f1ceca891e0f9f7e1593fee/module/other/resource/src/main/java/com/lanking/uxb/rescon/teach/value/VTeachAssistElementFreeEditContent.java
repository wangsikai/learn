package com.lanking.uxb.rescon.teach.value;

import com.lanking.uxb.rescon.question.value.VQuestion;

import java.io.Serializable;

/**
 * 自由编辑元素Value
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementFreeEditContent implements Serializable {
	private static final long serialVersionUID = 2429551666520559591L;

	private long id;
	private String content;
	private int sequence;
	private VQuestion question;
	private long freeEditId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public VQuestion getQuestion() {
		return question;
	}

	public void setQuestion(VQuestion question) {
		this.question = question;
	}

	public long getFreeEditId() {
		return freeEditId;
	}

	public void setFreeEditId(long freeEditId) {
		this.freeEditId = freeEditId;
	}
}
