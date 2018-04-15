package com.lanking.uxb.service.zuoye.form;

/**
 * 教师选择教辅 form
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年11月4日 上午10:39:38
 */
public class BookChooseForm {

	private long bookId;
	private Long schoolId;

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

}
