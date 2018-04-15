package com.lanking.uxb.rescon.book.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.common.resource.book.BookHistory.OperateType;
import com.lanking.uxb.rescon.account.value.VVendorUser;

/**
 * 书本操作历史.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月31日
 */
public class VBookHistory implements Serializable {
	private static final long serialVersionUID = 2426234262797724245L;

	private Long id;

	/**
	 * 书本ID.
	 */
	private Long bookId;

	/**
	 * 操作状态.
	 */
	private OperateType type;

	/**
	 * 操作人.
	 */
	private VVendorUser creator;

	/**
	 * 操作版本.
	 */
	private Integer version;

	/**
	 * 操作时间.
	 */
	private Date createAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public OperateType getType() {
		return type;
	}

	public void setType(OperateType type) {
		this.type = type;
	}

	public VVendorUser getCreator() {
		return creator;
	}

	public void setCreator(VVendorUser creator) {
		this.creator = creator;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
