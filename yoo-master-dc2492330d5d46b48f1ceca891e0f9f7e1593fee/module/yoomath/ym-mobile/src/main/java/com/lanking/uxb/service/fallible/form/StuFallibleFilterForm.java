package com.lanking.uxb.service.fallible.form;

import java.util.List;

import com.lanking.cloud.domain.common.resource.question.Question;

/**
 * 学生错题过滤条件form
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年5月4日
 */
public class StuFallibleFilterForm {
	// 查询过滤类型 ALL: 全部错题 OCR: 拍照错题 OTHER: 其他错题 TEXTBOOK: 教材错题
	private String type;
	// 若查询为了教材下的错题则此必须要传
	private Long sectionCode;
	// 表示几个月内的错题(全部错题按时间进行查询)
	// 0 表示所有时间范围 1 为一个月内 2为两个月内 3为三个月内
	private Integer month;
	// 查询数据大小
	private Integer size;
	// 游标
	private Long cursor;
	// 当Type == All的时候使用
	private Integer categoryCode;
	// 当Type == TEXTBOOK 时textbookCode 与 sectionCode中必须传一项
	private Integer textbookCode;
	// 过滤题目题型
	private List<Question.Type> questionTypes;
	// >=错误次数
	private Integer wrongTime;
	// 是否是做错题进行题目拉取
	private Boolean pullDoQuestions = false;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Long getCursor() {
		return cursor;
	}

	public void setCursor(Long cursor) {
		this.cursor = cursor;
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

	public List<Question.Type> getQuestionTypes() {
		return questionTypes;
	}

	public void setQuestionTypes(List<Question.Type> questionTypes) {
		this.questionTypes = questionTypes;
	}

	public Integer getWrongTime() {
		return wrongTime;
	}

	public void setWrongTime(Integer wrongTime) {
		this.wrongTime = wrongTime;
	}

	public Boolean getPullDoQuestions() {
		return pullDoQuestions;
	}

	public void setPullDoQuestions(Boolean pullDoQuestions) {
		this.pullDoQuestions = pullDoQuestions;
	}
}
