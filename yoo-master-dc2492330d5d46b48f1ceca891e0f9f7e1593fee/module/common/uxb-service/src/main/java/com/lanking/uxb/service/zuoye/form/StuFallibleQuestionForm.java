package com.lanking.uxb.service.zuoye.form;

import java.util.List;

/**
 * 学生错题查询表单
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月17日
 */
public class StuFallibleQuestionForm {

	private Integer pageNo;
	private Integer size;
	private Integer textbookCode;
	private List<Long> sectionCodes;
	private Boolean isUpdateAtDesc;
	private Boolean isCreateAtDesc;
	private Boolean isMistakeNumDesc;

	public Integer getPageNo() {
		if (pageNo == null || pageNo < 0) {
			return 1;
		}
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getSize() {
		if (size == null || size > 40 || size < 0) {
			return 40;
		}
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
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

}
