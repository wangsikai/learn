package com.lanking.uxb.rescon.teach.form;

import java.io.Serializable;
import java.util.List;

/**
 * 教辅表单.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version v1.3
 */
public class TeachAssistForm implements Serializable {
	private static final long serialVersionUID = -8218724003105097285L;

	/**
	 * 教辅ID.
	 */
	private Long teachAssistId;

	/**
	 * 教辅版本ID.
	 */
	private Long teachAssistVersionId;

	/**
	 * 封面.
	 */
	private Long coverId;

	/**
	 * 教辅名称.
	 */
	private String name;

	/**
	 * 阶段.
	 */
	private Integer phaseCode;

	/**
	 * 科目.
	 */
	private Integer subjectCode;

	/**
	 * 教材版本.
	 */
	private Integer textbookCategoryCode;

	/**
	 * 教材.
	 */
	private Integer textbookCode;

	/**
	 * 顺序章节.
	 */
	private List<Long> sectionCodes;

	/**
	 * 描述.
	 */
	private String description;

	/**
	 * 学校ID.
	 */
	private Long schoolId;
	/**
	 * 版本号
	 */
	private int version;

	public Long getTeachAssistId() {
		return teachAssistId;
	}

	public void setTeachAssistId(Long teachAssistId) {
		this.teachAssistId = teachAssistId;
	}

	public Long getTeachAssistVersionId() {
		return teachAssistVersionId;
	}

	public void setTeachAssistVersionId(Long teachAssistVersionId) {
		this.teachAssistVersionId = teachAssistVersionId;
	}

	public Long getCoverId() {
		return coverId;
	}

	public void setCoverId(Long coverId) {
		this.coverId = coverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(List<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
