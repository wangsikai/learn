package com.lanking.uxb.zycon.base.value;

import java.io.Serializable;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 教材习题VO
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年8月31日 上午11:13:53
 */
public class CTextbookExercise implements Serializable {

	private static final long serialVersionUID = -9121993174979579008L;

	private long id;
	private int textbookCode;
	private long sectionCode;
	private String name;
	/**
	 * yoomath V1.4 预置习题状态
	 */
	private Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(int textbookCode) {
		this.textbookCode = textbookCode;
	}

	public long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
