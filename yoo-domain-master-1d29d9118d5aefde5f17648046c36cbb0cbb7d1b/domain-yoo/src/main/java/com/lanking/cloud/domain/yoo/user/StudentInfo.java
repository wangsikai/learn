package com.lanking.cloud.domain.yoo.user;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 学生用户公共信息
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public class StudentInfo extends UserInfo {

	private static final long serialVersionUID = 2183987452086468879L;

	/**
	 * 学校ID
	 */
	@Column(name = "school_id")
	private Long schoolId;

	/**
	 * 阶段ID
	 */
	@Column(name = "phase_code")
	private Integer phaseCode;

	/**
	 * 学生入学年份.
	 */
	@Column(name = "enter_year")
	private Integer year;

	/**
	 * 版本代码
	 */
	@Column(name = "textbook_category_code")
	private Integer textbookCategoryCode;

	/**
	 * 教材代码
	 */
	@Column(name = "textbook_code")
	private Integer textbookCode;

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getTextbookCategoryCode() {
		return textbookCategoryCode;
	}

	public void setTextbookCategoryCode(Integer textbookCategoryCode) {
		this.textbookCategoryCode = textbookCategoryCode;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

}
