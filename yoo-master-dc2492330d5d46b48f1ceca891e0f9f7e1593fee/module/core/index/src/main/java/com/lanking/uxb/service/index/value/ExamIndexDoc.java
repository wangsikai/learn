package com.lanking.uxb.service.index.value;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.uxb.service.index.api.IndexMapping;
import com.lanking.uxb.service.index.api.IndexType;
import com.lanking.uxb.service.index.api.MappingType;

/**
 * 试卷索引 VO
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月23日 下午2:52:57
 */
@Component
@IndexType(type = com.lanking.cloud.domain.type.IndexType.EXAM_PAPER)
public class ExamIndexDoc extends AbstraceIndexDoc {

	private static final long serialVersionUID = -3119913795231188798L;

	@IndexMapping(type = MappingType.LONG)
	private Long id;

	@IndexMapping(type = MappingType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String name;

	@IndexMapping(type = MappingType.LONG)
	private Long createId;

	@IndexMapping(type = MappingType.INTEGER)
	private Integer status;

	@IndexMapping(type = MappingType.LONG)
	private Long createAt;

	@IndexMapping(type = MappingType.INTEGER)
	private Integer phaseCode;

	@IndexMapping(type = MappingType.INTEGER)
	private Integer subjectCode;

	@IndexMapping(type = MappingType.INTEGER)
	private Integer category;

	@IndexMapping(type = MappingType.LONG)
	private Long districtCode;

	@IndexMapping(type = MappingType.INTEGER)
	private Integer year;

	@IndexMapping(type = MappingType.LONG)
	private Long schoolId;

	@IndexMapping(type = MappingType.TEXT)
	private String schoolName;

	@IndexMapping(type = MappingType.LONG)
	private Long ownSchoolId;

	@IndexMapping(type = MappingType.DOUBLE)
	private BigDecimal difficulty;

	@IndexMapping(type = MappingType.INTEGER)
	private Integer textbookCode;

	@IndexMapping(type = MappingType.INTEGER)
	private Integer textbookcategoryCode;

	@IndexMapping(type = MappingType.LONG)
	private Long sectionCode;

	@IndexMapping(type = MappingType.LONG)
	private Long vendorId;

	@IndexMapping(type = MappingType.INTEGER)
	private Integer exampaperGoodsStatus;

	@IndexMapping(type = MappingType.INTEGER)
	private Integer isRecommend;

	// 顺序章节
	@IndexMapping(type = MappingType.LONG, ignore = true)
	private List<Long> sectionCodes = Lists.newArrayList();

	@IndexMapping(type = MappingType.LONG)
	private Long clickCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
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

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Long getOwnSchoolId() {
		return ownSchoolId;
	}

	public void setOwnSchoolId(Long ownSchoolId) {
		this.ownSchoolId = ownSchoolId;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public Integer getTextbookcategoryCode() {
		return textbookcategoryCode;
	}

	public void setTextbookcategoryCode(Integer textbookcategoryCode) {
		this.textbookcategoryCode = textbookcategoryCode;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getExampaperGoodsStatus() {
		return exampaperGoodsStatus;
	}

	public void setExampaperGoodsStatus(Integer exampaperGoodsStatus) {
		this.exampaperGoodsStatus = exampaperGoodsStatus;
	}

	public Integer getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(Integer isRecommend) {
		this.isRecommend = isRecommend;
	}

	public List<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(List<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public Long getClickCount() {
		return clickCount;
	}

	public void setClickCount(Long clickCount) {
		this.clickCount = clickCount;
	}

}
