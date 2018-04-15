package com.lanking.uxb.service.zuoye.api;

import java.math.BigDecimal;
import java.util.List;

public class ZyQuestionCollectQuery {
	/**
	 * 学科.
	 */
	private Integer subjectCode;

	/**
	 * 题型.
	 */
	private Long typeCode;

	/**
	 * 题型.
	 */
	private List<Long> typeCodes;

	/**
	 * 教材版本.
	 */
	private Integer categoryCode;
	/**
	 * 排序方式. 0：正序，1：倒序.
	 */
	private Integer sort = 0;
	/**
	 * 章节码
	 */
	private Long sectionCode;
	/**
	 * 版本
	 */
	private Integer textbookCode;
	/**
	 * 知识点code.
	 */
	private Integer metaknowCode;
	/**
	 * 最小难度
	 */
	private BigDecimal leDifficulty;
	/**
	 * 最大难度
	 */
	private BigDecimal reDifficulty;
	/**
	 * 用户id
	 */
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BigDecimal getLeDifficulty() {
		return leDifficulty;
	}

	public void setLeDifficulty(BigDecimal leDifficulty) {
		this.leDifficulty = leDifficulty;
	}

	public BigDecimal getReDifficulty() {
		return reDifficulty;
	}

	public void setReDifficulty(BigDecimal reDifficulty) {
		this.reDifficulty = reDifficulty;
	}

	public Integer getMetaknowCode() {
		return metaknowCode;
	}

	public void setMetaknowCode(Integer metaknowCode) {
		this.metaknowCode = metaknowCode;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Long getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Long typeCode) {
		this.typeCode = typeCode;
	}

	public Integer getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(Integer categoryCode) {
		this.categoryCode = categoryCode;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public List<Long> getTypeCodes() {
		return typeCodes;
	}

	public void setTypeCodes(List<Long> typeCodes) {
		this.typeCodes = typeCodes;
	}

}
