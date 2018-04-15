package com.lanking.uxb.service.fallible.form;

import com.lanking.cloud.domain.common.resource.question.Question.Type;

/**
 * 教师错题过滤条件form
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年5月4日
 */
public class TeaFallibleFilterForm {

	private Integer textbookCode;
	private OrderType orderType;
	private Long sectionCode;
	private Integer minRightRate;
	private Integer maxRightRate;
	private UpdateRange updateRange;
	private Type questionType;
	private DifficultyType difficultyType;
	private Integer pageNo = 1;
	private Long endTime;
	private Long cursor;

	public enum OrderType {
		UPDATE_TIME_DESC, UPDATE_TIME_ASC, RIGHT_RATE_DESC, RIGHT_RATE_ASC;
	}

	public enum UpdateRange {
		A_WEEK, A_MONTH;
	}

	public enum DifficultyType {
		BASIS, IMPROVE, HARD;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Integer getMinRightRate() {
		return minRightRate;
	}

	public void setMinRightRate(Integer minRightRate) {
		this.minRightRate = minRightRate;
	}

	public Integer getMaxRightRate() {
		return maxRightRate;
	}

	public void setMaxRightRate(Integer maxRightRate) {
		this.maxRightRate = maxRightRate;
	}

	public UpdateRange getUpdateRange() {
		return updateRange;
	}

	public void setUpdateRange(UpdateRange updateRange) {
		this.updateRange = updateRange;
	}

	public Type getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Type questionType) {
		this.questionType = questionType;
	}

	public DifficultyType getDifficultyType() {
		return difficultyType;
	}

	public void setDifficultyType(DifficultyType difficultyType) {
		this.difficultyType = difficultyType;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getCursor() {
		return cursor;
	}

	public void setCursor(Long cursor) {
		this.cursor = cursor;
	}

}
