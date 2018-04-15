package com.lanking.uxb.service.user.form;

/**
 * 完善个人资料提交
 * 
 * @author wangsenhao
 *
 */
public class PerfectDataForm {

	private String name;
	private Long schoolCode;
	private String schoolName;
	private Integer phaseCode;
	private Integer textBookCode;
	private Integer textBookCategoryCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(Long schoolCode) {
		this.schoolCode = schoolCode;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getTextBookCode() {
		return textBookCode;
	}

	public void setTextBookCode(Integer textBookCode) {
		this.textBookCode = textBookCode;
	}

	public Integer getTextBookCategoryCode() {
		return textBookCategoryCode;
	}

	public void setTextBookCategoryCode(Integer textBookCategoryCode) {
		this.textBookCategoryCode = textBookCategoryCode;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

}
