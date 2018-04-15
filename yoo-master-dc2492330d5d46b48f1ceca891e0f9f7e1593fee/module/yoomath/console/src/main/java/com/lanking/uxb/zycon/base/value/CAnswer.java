package com.lanking.uxb.zycon.base.value;

import java.io.Serializable;

/**
 * 答案VO
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年8月31日 上午11:13:22
 */
public class CAnswer implements Serializable {

	private static final long serialVersionUID = -6533290749471544324L;

	private long id;
	private long questionId;
	private int sequence;
	private String content;

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

}
