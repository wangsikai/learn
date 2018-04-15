package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.Date;

/**
 * 学生错题查询条件
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月16日
 */
public class ZyStudentFallibleQuestionQuery {
	private long studentId;
	private Integer textbookCode;
	private Collection<Long> sectionCodes;
	private Long sectionCode;
	private Boolean isUpdateAtDesc;
	private Boolean isCreateAtDesc;
	private Boolean isMistakeNumDesc;
	// 是否查询其他错题(非此章节目录下的)
	private Boolean isOther;
	private Boolean isOcr;
	private Boolean isAll;
	private Integer categoryCode;

	private Date updateAtCursor;
	private Date createAtCursor;

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public Collection<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(Collection<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Boolean getIsUpdateAtDesc() {
		return isUpdateAtDesc;
	}

	public void setIsUpdateAtDesc(Boolean isUpdateAtDesc) {
		this.isUpdateAtDesc = isUpdateAtDesc;
	}

	public Boolean getIsCreateAtDesc() {
		return isCreateAtDesc;
	}

	public void setIsCreateAtDesc(Boolean isCreateAtDesc) {
		this.isCreateAtDesc = isCreateAtDesc;
	}

	public Boolean getIsMistakeNumDesc() {
		return isMistakeNumDesc;
	}

	public void setIsMistakeNumDesc(Boolean isMistakeNumDesc) {
		this.isMistakeNumDesc = isMistakeNumDesc;
	}

	public Date getUpdateAtCursor() {
		return updateAtCursor;
	}

	public void setUpdateAtCursor(Date updateAtCursor) {
		this.updateAtCursor = updateAtCursor;
	}

	public Date getCreateAtCursor() {
		return createAtCursor;
	}

	public void setCreateAtCursor(Date createAtCursor) {
		this.createAtCursor = createAtCursor;
	}

	public Boolean getOther() {
		return isOther;
	}

	public void setOther(Boolean other) {
		isOther = other;
	}

	public Boolean getOcr() {
		return isOcr;
	}

	public void setOcr(Boolean ocr) {
		isOcr = ocr;
	}

	public Integer getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(Integer categoryCode) {
		this.categoryCode = categoryCode;
	}

	public Boolean getAll() {
		return isAll;
	}

	public void setAll(Boolean all) {
		isAll = all;
	}
}
