package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;

import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExerciseType;

/**
 * 教材习题VO
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月15日
 */
public class VTextbookExercise implements Serializable {

	private static final long serialVersionUID = -9121993174979579008L;

	private long id;
	private int textbookCode;
	private long sectionCode;
	private String name;
	private TextbookExerciseType type;

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

	public TextbookExerciseType getType() {
		return type;
	}

	public void setType(TextbookExerciseType type) {
		this.type = type;
	}

}
