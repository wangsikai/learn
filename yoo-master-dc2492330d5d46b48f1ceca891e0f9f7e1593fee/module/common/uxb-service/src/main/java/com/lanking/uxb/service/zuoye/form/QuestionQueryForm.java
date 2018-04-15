package com.lanking.uxb.service.zuoye.form;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 题目查询
 * 
 * @since yoomath V1.3
 * @author wangsenhao
 * @version 2015年9月6日
 */
public class QuestionQueryForm {

	/**
	 * 学科.
	 */
	private Integer subjectCode;

	/**
	 * 题型.
	 */
	private Long typeCode;

	private Set<Long> typeCodes;

	/**
	 * 教材版本.
	 */
	private Integer categoryCode;

	/**
	 * 最小正确率
	 */
	private BigDecimal leRightRate;
	/**
	 * 最大正确率
	 */
	private BigDecimal reRightRate;
	/**
	 * 最小难度
	 */
	private BigDecimal leDifficulty;
	/**
	 * 最大难度
	 */
	private BigDecimal reDifficulty;

	/**
	 * 排序类型. 1：创建时间，2：难度系数
	 */
	private Integer sortType = 1;

	/**
	 * 排序方式. 0：正序，1：倒序.
	 */
	private Integer sort = 0;

	/**
	 * 知识点code.
	 */
	private Integer metaknowCode;

	/**
	 * 知识点code.
	 */
	private List<Integer> metaknowCodes;
	/**
	 * 新知识点
	 */
	private List<Long> newKnowpointCodes;

	/**
	 * 章节码
	 */
	private Long sectionCode;
	/**
	 * 版本
	 */
	private Integer textbookCode;
	/**
	 * 时间范围:不设置表示全部,1:表示最近一周2:表示最近一个月
	 */
	private Integer timeRange;
	private Boolean isCreateAtDesc;
	private Boolean isRightRateDesc;

	private Integer pageSize = 20;
	private Integer page = 1;

	/**
	 * 以下记录难度标签开闭合
	 */
	private boolean leftOpen = false;

	private boolean rightOpen = false;

	/**
	 * 以下记录正确率标签开闭合
	 */
	private boolean rateleftOpen = false;
	private boolean raterightOpen = false;

	private boolean searchInSection = false;

	private List<Long> sectionCodes;

	private Long userId;
	/**
	 * yoomath V1.4.2 <br>
	 * 关键字,题库新增搜索功能
	 */
	private String key;
	/**
	 * 新增学校id
	 */
	private Long schoolId;
	/**
	 * 题目的标签
	 */
	private List<Integer> categoryTypes;

	private Long knowledgeCode;
	// @since 2.3.0 是否按照新知识点查询数据
	private Boolean newKeyQuery = false;

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
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

	public BigDecimal getLeRightRate() {
		return leRightRate;
	}

	public void setLeRightRate(BigDecimal leRightRate) {
		this.leRightRate = leRightRate;
	}

	public BigDecimal getReRightRate() {
		return reRightRate;
	}

	public void setReRightRate(BigDecimal reRightRate) {
		this.reRightRate = reRightRate;
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

	public Integer getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(Integer timeRange) {
		this.timeRange = timeRange;
	}

	public Integer getSortType() {
		return sortType;
	}

	public void setSortType(Integer sortType) {
		this.sortType = sortType;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

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

	public Long getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Long typeCode) {
		this.typeCode = typeCode;
	}

	public Integer getMetaknowCode() {
		return metaknowCode;
	}

	public void setMetaknowCode(Integer metaknowCode) {
		this.metaknowCode = metaknowCode;
	}

	public Boolean getIsRightRateDesc() {
		return isRightRateDesc;
	}

	public void setIsRightRateDesc(Boolean isRightRateDesc) {
		this.isRightRateDesc = isRightRateDesc;
	}

	public Boolean getIsCreateAtDesc() {
		return isCreateAtDesc;
	}

	public void setIsCreateAtDesc(Boolean isCreateAtDesc) {
		this.isCreateAtDesc = isCreateAtDesc;
	}

	public List<Integer> getMetaknowCodes() {
		return metaknowCodes;
	}

	public void setMetaknowCodes(List<Integer> metaknowCodes) {
		this.metaknowCodes = metaknowCodes;
	}

	public boolean isLeftOpen() {
		return leftOpen;
	}

	public void setLeftOpen(boolean leftOpen) {
		this.leftOpen = leftOpen;
	}

	public boolean isRightOpen() {
		return rightOpen;
	}

	public void setRightOpen(boolean rightOpen) {
		this.rightOpen = rightOpen;
	}

	public boolean isRaterightOpen() {
		return raterightOpen;
	}

	public void setRaterightOpen(boolean raterightOpen) {
		this.raterightOpen = raterightOpen;
	}

	public boolean isRateleftOpen() {
		return rateleftOpen;
	}

	public void setRateleftOpen(boolean rateleftOpen) {
		this.rateleftOpen = rateleftOpen;
	}

	public List<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(List<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public boolean isSearchInSection() {
		return searchInSection;
	}

	public void setSearchInSection(boolean searchInSection) {
		this.searchInSection = searchInSection;
	}

	public Set<Long> getTypeCodes() {
		return typeCodes;
	}

	public void setTypeCodes(Set<Long> typeCodes) {
		this.typeCodes = typeCodes;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getKnowledgeCode() {
		return knowledgeCode;
	}

	public void setKnowledgeCode(Long knowledgeCode) {
		this.knowledgeCode = knowledgeCode;
	}

	public List<Integer> getCategoryTypes() {
		return categoryTypes;
	}

	public void setCategoryTypes(List<Integer> categoryTypes) {
		this.categoryTypes = categoryTypes;
	}

	public Boolean getNewKeyQuery() {
		return newKeyQuery;
	}

	public void setNewKeyQuery(Boolean newKeyQuery) {
		this.newKeyQuery = newKeyQuery;
	}

	public List<Long> getNewKnowpointCodes() {
		return newKnowpointCodes;
	}

	public void setNewKnowpointCodes(List<Long> newKnowpointCodes) {
		this.newKnowpointCodes = newKnowpointCodes;
	}

}
