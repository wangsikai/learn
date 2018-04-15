package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;

import com.lanking.cloud.domain.common.resource.book.BookOpenStatus;

/**
 * 书本VO
 * 
 * @since yoomath V1.6
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年11月3日 下午5:20:46
 */
public class VBookVersion implements Serializable {

	private static final long serialVersionUID = 3068002087612491227L;

	private long id;
	private long bookId;
	private String name;
	private String url;
	private Long schoolId;
	/**
	 * 该书本在该版本教材下是否被选择
	 */
	private boolean selected;
	// 开放状态
	private BookOpenStatus openStatus;
	/**
	 * 书本简称
	 */
	private String shortName;
	/**
	 * 有标签就显示，没有就不显示
	 */
	private String tagName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

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

	public BookOpenStatus getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(BookOpenStatus openStatus) {
		this.openStatus = openStatus;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

}
