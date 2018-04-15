package com.lanking.uxb.rescon.statistics.form;

import java.io.Serializable;

/**
 * 题目统计查询表单
 * 
 * @since 2.1
 * @author wangsenhao
 *
 */
public class QuestionStatisForm implements Serializable {

	private static final long serialVersionUID = -230833759635968761L;

	private Integer phaseCode;
	private Integer subjectCode;
	private Integer categoryCode;
	private Integer textbookCode;
	private String key;
	// 默认查询旧知识点
	private int version = 1;

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Integer getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(Integer categoryCode) {
		this.categoryCode = categoryCode;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
