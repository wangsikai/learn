package com.lanking.cloud.domain.yoo.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 教师用户公共信息
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public class TeacherInfo extends UserInfo {

	private static final long serialVersionUID = -2308146954246605650L;

	/**
	 * 学科代码
	 */
	@Column(name = "subject_code", precision = 11)
	private Integer subjectCode;

	/**
	 * 学校ID
	 */
	@Column(name = "school_id")
	private Long schoolId;

	/**
	 * 阶段代码
	 */
	@Column(name = "phase_code")
	private Integer phaseCode;

	/**
	 * 工作时间
	 */
	@Column(name = "work_at", columnDefinition = "datetime(3)")
	private Date workAt;

	/**
	 * 职称代码
	 */
	@Column(name = "title_code", precision = 11)
	private Integer titleCode;

	/**
	 * 职务代码
	 */
	@Column(name = "duty_code", precision = 11)
	private Integer dutyCode;

	/**
	 * 学校名称
	 */
	@Column(name = "school_name", length = 40)
	private String schoolName;

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

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

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

	public Date getWorkAt() {
		return workAt;
	}

	public void setWorkAt(Date workAt) {
		this.workAt = workAt;
	}

	public Integer getTitleCode() {
		return titleCode;
	}

	public void setTitleCode(Integer titleCode) {
		this.titleCode = titleCode;
	}

	public Integer getDutyCode() {
		return dutyCode;
	}

	public void setDutyCode(Integer dutyCode) {
		this.dutyCode = dutyCode;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
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
