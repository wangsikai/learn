package com.lanking.uxb.zycon.book.form;

import java.io.Serializable;
import java.util.List;

import com.lanking.cloud.domain.common.resource.book.BookOpenStatus;

/**
 * 书本状态设置FORM
 * 
 * @author wangsenhao
 *
 */
public class BookForm implements Serializable {

	private static final long serialVersionUID = -6671026861645516138L;
	/**
	 * 书本版本id
	 */
	private Long bookVersionId;
	/**
	 * 书本id
	 */
	private Long bookId;
	/**
	 * 书本开放状态
	 */
	private BookOpenStatus openStatus;
	/**
	 * 学校id集合
	 */
	private List<Long> schoolIds;

	public Long getBookVersionId() {
		return bookVersionId;
	}

	public void setBookVersionId(Long bookVersionId) {
		this.bookVersionId = bookVersionId;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public BookOpenStatus getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(BookOpenStatus openStatus) {
		this.openStatus = openStatus;
	}

	public List<Long> getSchoolIds() {
		return schoolIds;
	}

	public void setSchoolIds(List<Long> schoolIds) {
		this.schoolIds = schoolIds;
	}

}
