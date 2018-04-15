package com.lanking.uxb.zycon.mall.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VResourceCategory;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;

public class VZycExam implements Serializable {
	private static final long serialVersionUID = 1506264636206283648L;
	private Long id;
	private String name;
	private VSubject subject;
	private String district = "";
	private VPhase phase;
	private VResourceCategory category;
	private Integer year;
	private VSchool school;
	/**
	 * 校本学校.
	 */
	private VSchool ownSchool;
	private BigDecimal difficulty = new BigDecimal(-1.0);
	private VTextbook textbook;
	private VTextbookCategory textbookCategory;
	private String section;
	private Long sectionCode;
	private Integer score;
	private Integer questionCount = 0;
	private Date createAt;
	private String creator;
	private Long districtCode;
	// 校验通过的题目数量
	private Integer questionPassCount = 0;

	// 资源商品
	private ResourcesGoods resourcesGoods;
	private BigDecimal price;
	private BigDecimal priceRMB;

	// 资源分类

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

	public VSubject getSubject() {
		return subject;
	}

	public void setSubject(VSubject subject) {
		this.subject = subject;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public VPhase getPhase() {
		return phase;
	}

	public void setPhase(VPhase phase) {
		this.phase = phase;
	}

	public VResourceCategory getCategory() {
		return category;
	}

	public void setCategory(VResourceCategory category) {
		this.category = category;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public VSchool getSchool() {
		return school;
	}

	public void setSchool(VSchool school) {
		this.school = school;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public VTextbook getTextbook() {
		return textbook;
	}

	public void setTextbook(VTextbook textbook) {
		this.textbook = textbook;
	}

	public VTextbookCategory getTextbookCategory() {
		return textbookCategory;
	}

	public void setTextbookCategory(VTextbookCategory textbookCategory) {
		this.textbookCategory = textbookCategory;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public Long getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(Long districtCode) {
		this.districtCode = districtCode;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public VSchool getOwnSchool() {
		return ownSchool;
	}

	public void setOwnSchool(VSchool ownSchool) {
		this.ownSchool = ownSchool;
	}

	public Integer getQuestionPassCount() {
		return questionPassCount;
	}

	public void setQuestionPassCount(Integer questionPassCount) {
		this.questionPassCount = questionPassCount;
	}

	public ResourcesGoods getResourcesGoods() {
		return resourcesGoods;
	}

	public void setResourcesGoods(ResourcesGoods resourcesGoods) {
		this.resourcesGoods = resourcesGoods;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPriceRMB() {
		return priceRMB;
	}

	public void setPriceRMB(BigDecimal priceRMB) {
		this.priceRMB = priceRMB;
	}

}
