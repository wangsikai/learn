package com.lanking.uxb.zycon.book.api;

import java.io.Serializable;

import com.lanking.cloud.domain.common.resource.book.BookOpenStatus;

/**
 * 书本查询条件
 * 
 * @author wangsenhao
 *
 */
public class BookQuery implements Serializable {

	private static final long serialVersionUID = -5225626978112750280L;
	/**
	 * 搜索的范围为书本名称、ISBN编号、书本编号
	 */
	private String key;
	/**
	 * 科目
	 */
	private Integer subjectCode;
	/**
	 * 书本类型
	 */
	private Integer bookType;
	/**
	 * 书本状态
	 */
	private BookOpenStatus openStatus;
	/**
	 * 章节码
	 */
	private Long sectionCode;
	/**
	 * 教材
	 */
	private Integer textbookCode;
	/**
	 * 版本
	 */
	private Integer categoryCode;
	/**
	 * 是否需要过滤掉完全公开的书本<br>
	 * 校本题库增加书本时，对于完全公开的图书，不可以设置为校本图书
	 */
	private boolean statusfilter = false;

	private Long schoolId;
	private Integer pageSize = 10;
	private Integer page = 1;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Integer getBookType() {
		return bookType;
	}

	public void setBookType(Integer bookType) {
		this.bookType = bookType;
	}

	public BookOpenStatus getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(BookOpenStatus openStatus) {
		this.openStatus = openStatus;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public Integer getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(Integer categoryCode) {
		this.categoryCode = categoryCode;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public boolean isStatusfilter() {
		return statusfilter;
	}

	public void setStatusfilter(boolean statusfilter) {
		this.statusfilter = statusfilter;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}
}
