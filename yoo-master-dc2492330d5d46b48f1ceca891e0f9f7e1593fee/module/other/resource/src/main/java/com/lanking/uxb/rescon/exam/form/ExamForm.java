package com.lanking.uxb.rescon.exam.form;

/**
 * 创建试卷 form
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月21日 下午3:08:36
 */
public class ExamForm {

	private Long id;

	private String name;

	private Integer typeCode;

	private Integer phaseCode;

	private Integer subjectCode;

	private Integer textBookCategoryCode;

	private Integer textBookCode;

	private Long sectionCode;

	private Long districtCode;

	private Integer year;

	private Long schoolId;

	private Long ownSchoolId;

	private Double difficulty = 0.0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
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

	public Integer getTextBookCategoryCode() {
		return textBookCategoryCode;
	}

	public void setTextBookCategoryCode(Integer textBookCategoryCode) {
		this.textBookCategoryCode = textBookCategoryCode;
	}

	public Integer getTextBookCode() {
		return textBookCode;
	}

	public void setTextBookCode(Integer textBookCode) {
		this.textBookCode = textBookCode;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
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

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwnSchoolId() {
		return ownSchoolId;
	}

	public void setOwnSchoolId(Long ownSchoolId) {
		this.ownSchoolId = ownSchoolId;
	}
}
