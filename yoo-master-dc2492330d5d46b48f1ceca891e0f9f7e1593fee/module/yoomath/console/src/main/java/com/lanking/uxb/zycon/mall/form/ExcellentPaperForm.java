package com.lanking.uxb.zycon.mall.form;

import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsStatus;

/**
 * 查询试卷商品form
 * 
 * @since 2.0.7
 * @author zemin.song
 * @version 2016年9月6日 17:54:34
 */
public class ExcellentPaperForm {

	private String key;
	// 创建人
	private Long createId;
	private ResourcesGoodsStatus status;
	// 创建时间 范围搜索 开始时间
	private Long startCreateAt;
	// 创建时间 范围搜索 结束时间
	private Long endCreateAt;
	private Integer phaseCode;
	private Integer subjectCode;
	private Integer category;
	private Long districtCode;
	private Integer year;
	private Long schoolId;
	private Double minDifficulty;
	private Double maxDifficulty;
	private Long textBookCode;
	private Long textBookCategoryCode;
	private Long sectionCode;
	private Integer pageSize = 20;
	private Integer page = 1;
	private Long vendorId;
	private String examCode;
	private Boolean needCount = false;
	private String orderBy;
	// 当为true的时候是降序,当为false的时候是升序
	private Boolean order;
	// 学校名称
	private String schoolName;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Long getStartCreateAt() {
		return startCreateAt;
	}

	public void setStartCreateAt(Long startCreateAt) {
		this.startCreateAt = startCreateAt;
	}

	public Long getEndCreateAt() {
		return endCreateAt;
	}

	public void setEndCreateAt(Long endCreateAt) {
		this.endCreateAt = endCreateAt;
	}

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

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public Long getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(Long districtCode) {
		this.districtCode = districtCode;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Double getMinDifficulty() {
		return minDifficulty;
	}

	public void setMinDifficulty(Double minDifficulty) {
		this.minDifficulty = minDifficulty;
	}

	public Double getMaxDifficulty() {
		return maxDifficulty;
	}

	public void setMaxDifficulty(Double maxDifficulty) {
		this.maxDifficulty = maxDifficulty;
	}

	public Long getTextBookCode() {
		return textBookCode;
	}

	public void setTextBookCode(Long textBookCode) {
		this.textBookCode = textBookCode;
	}

	public Long getTextBookCategoryCode() {
		return textBookCategoryCode;
	}

	public void setTextBookCategoryCode(Long textBookCategoryCode) {
		this.textBookCategoryCode = textBookCategoryCode;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
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

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getExamCode() {
		return examCode;
	}

	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}

	public Boolean getNeedCount() {
		return needCount;
	}

	public void setNeedCount(Boolean needCount) {
		this.needCount = needCount;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Boolean getOrder() {
		return order;
	}

	public void setOrder(Boolean order) {
		this.order = order;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public ResourcesGoodsStatus getStatus() {
		return status;
	}

	public void setStatus(ResourcesGoodsStatus status) {
		this.status = status;
	}

}
