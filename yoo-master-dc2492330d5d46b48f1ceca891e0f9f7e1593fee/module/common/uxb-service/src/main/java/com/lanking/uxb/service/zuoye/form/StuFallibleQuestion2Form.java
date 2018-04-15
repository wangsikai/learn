package com.lanking.uxb.service.zuoye.form;

import java.util.List;
import java.util.Set;

/**
 * 学生错题查询表单
 * 
 * @since yoomathV2.0
 * @author wangsenhao
 *
 */
public class StuFallibleQuestion2Form {
	private Integer pageSize = 20;
	private Integer page = 1;
	/**
	 * 30,90,180天内的错题
	 */
	private Integer day;
	private Long typeCode;
	/**
	 * 题目错误次数
	 */
	private Long mistakeNum;
	/**
	 * 错题来源
	 */
	private Integer source;
	/**
	 * 关键字搜索
	 */
	private String key;

	private Integer categoryCode;

	private Integer textbookCode;
	/**
	 * 知识点code.
	 */
	private Integer metaknowCode;
	/**
	 * 知识点code.
	 */
	private List<Integer> metaknowCodes;
	/**
	 * 新的知识点
	 */
	private Long newKnowCode;
	/**
	 * 章节码
	 */
	private Long sectionCode;

	private List<Long> sectionCodes;

	private Set<Long> typeCodes;
	// 题目类型
	private List<Integer> questionTypes;

	private Long userId;
	// 时间游标
	private Long updateAtCursor;
	// 是否是查询其他数据
	private Boolean other = false;

	private Boolean isKp = false;

	// 是否按照新知识点查询数据
	private Boolean newKeyQuery = false;
	// 是否按照创建时间进行排序
	private Boolean orderByCreateAt = true;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Long getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Long typeCode) {
		this.typeCode = typeCode;
	}

	public Long getMistakeNum() {
		return mistakeNum;
	}

	public void setMistakeNum(Long mistakeNum) {
		this.mistakeNum = mistakeNum;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public Integer getMetaknowCode() {
		return metaknowCode;
	}

	public void setMetaknowCode(Integer metaknowCode) {
		this.metaknowCode = metaknowCode;
	}

	public List<Integer> getMetaknowCodes() {
		return metaknowCodes;
	}

	public void setMetaknowCodes(List<Integer> metaknowCodes) {
		this.metaknowCodes = metaknowCodes;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public List<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(List<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public Set<Long> getTypeCodes() {
		return typeCodes;
	}

	public void setTypeCodes(Set<Long> typeCodes) {
		this.typeCodes = typeCodes;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Integer> getQuestionTypes() {
		return questionTypes;
	}

	public void setQuestionTypes(List<Integer> questionTypes) {
		this.questionTypes = questionTypes;
	}

	public Long getUpdateAtCursor() {
		return updateAtCursor;
	}

	public void setUpdateAtCursor(Long updateAtCursor) {
		this.updateAtCursor = updateAtCursor;
	}

	public Boolean getOther() {
		return other;
	}

	public void setOther(Boolean other) {
		this.other = other;
	}

	public Boolean getIsKp() {
		return isKp;
	}

	public void setIsKp(Boolean isKp) {
		this.isKp = isKp;
	}

	public Long getNewKnowCode() {
		return newKnowCode;
	}

	public void setNewKnowCode(Long newKnowCode) {
		this.newKnowCode = newKnowCode;
	}

	public Boolean getNewKeyQuery() {
		return newKeyQuery;
	}

	public void setNewKeyQuery(Boolean newKeyQuery) {
		this.newKeyQuery = newKeyQuery;
	}

	public Boolean getOrderByCreateAt() {
		return orderByCreateAt;
	}

	public void setOrderByCreateAt(Boolean orderByCreateAt) {
		this.orderByCreateAt = orderByCreateAt;
	}
}
