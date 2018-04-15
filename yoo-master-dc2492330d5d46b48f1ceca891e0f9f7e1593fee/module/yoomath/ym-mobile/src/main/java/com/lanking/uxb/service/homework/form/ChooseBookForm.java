package com.lanking.uxb.service.homework.form;

import java.util.List;

/**
 * 选择教辅图书接口form
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年4月18日
 */
public class ChooseBookForm {

	private Integer textbookCode;
	private List<Long> freeBookIds;
	/**
	 * 校级书本id集合
	 */
	private List<Long> schoolBookIds;
	/**
	 * bookId集合
	 */
	private List<Long> bookIds;

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public List<Long> getFreeBookIds() {
		return freeBookIds;
	}

	public void setFreeBookIds(List<Long> freeBookIds) {
		this.freeBookIds = freeBookIds;
	}

	public List<Long> getSchoolBookIds() {
		return schoolBookIds;
	}

	public void setSchoolBookIds(List<Long> schoolBookIds) {
		this.schoolBookIds = schoolBookIds;
	}

	public List<Long> getBookIds() {
		return bookIds;
	}

	public void setBookIds(List<Long> bookIds) {
		this.bookIds = bookIds;
	}

}
