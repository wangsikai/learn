package com.lanking.uxb.rescon.exam.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperStatus;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VResourceCategory;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;

public class VExam implements Serializable {
	private static final long serialVersionUID = 1506264636206283648L;
	private Long id;
	private String name;
	private ExamPaperStatus status;
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
	private List<VExamPaperTopic> topics;
	private Date createAt;
	private String creator;
	private List<VOperationUser> updators;
	private Long districtCode;
	// 校验通过的题目数量
	private Integer questionPassCount = 0;
	// 录入中的题目数量
	private Integer questionEditCount = 0;

	/**
	 * 未转换v3知识点的题目个数.
	 */
	private int noV3Count;

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

	public ExamPaperStatus getStatus() {
		return status;
	}

	public void setStatus(ExamPaperStatus status) {
		this.status = status;
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

	public List<VExamPaperTopic> getTopics() {
		return topics;
	}

	public void setTopics(List<VExamPaperTopic> topics) {
		this.topics = topics;
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

	public List<VOperationUser> getUpdators() {
		return updators;
	}

	public void setUpdators(List<VOperationUser> updators) {
		this.updators = updators;
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

	public Integer getQuestionEditCount() {
		return questionEditCount;
	}

	public void setQuestionEditCount(Integer questionEditCount) {
		this.questionEditCount = questionEditCount;
	}

	public int getNoV3Count() {
		return noV3Count;
	}

	public void setNoV3Count(int noV3Count) {
		this.noV3Count = noV3Count;
	}
}
