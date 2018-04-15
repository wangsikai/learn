package com.lanking.uxb.service.homework.form;

import java.util.List;

/**
 * 设置推荐作业进度参数
 * 
 * @since 2.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年5月30日
 */
public class SetProgressForm {

	private long bookVersionId;
	private long bookCataId;
	private List<Long> classIds;

	public long getBookVersionId() {
		return bookVersionId;
	}

	public void setBookVersionId(long bookVersionId) {
		this.bookVersionId = bookVersionId;
	}

	public long getBookCataId() {
		return bookCataId;
	}

	public void setBookCataId(long bookCataId) {
		this.bookCataId = bookCataId;
	}

	public List<Long> getClassIds() {
		return classIds;
	}

	public void setClassIds(List<Long> classIds) {
		this.classIds = classIds;
	}

}
