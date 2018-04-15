package com.lanking.uxb.rescon.question.value;

import java.io.Serializable;

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
	private String contentLatex;
	private String contentAscii;

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

	public String getContentLatex() {
		return contentLatex;
	}

	public void setContentLatex(String contentLatex) {
		this.contentLatex = contentLatex;
	}

	public String getContentAscii() {
		return contentAscii;
	}

	public void setContentAscii(String contentAscii) {
		this.contentAscii = contentAscii;
	}
}
