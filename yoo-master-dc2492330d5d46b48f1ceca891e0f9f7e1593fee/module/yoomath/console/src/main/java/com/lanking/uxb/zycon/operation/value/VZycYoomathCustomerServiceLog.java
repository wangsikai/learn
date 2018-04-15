package com.lanking.uxb.zycon.operation.value;

import java.io.Serializable;

/**
 * YoomathCustomerServiceLog value
 *
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
public class VZycYoomathCustomerServiceLog implements Serializable {
	private static final long serialVersionUID = -1577277312860951565L;

	private long id;
	private long userId;
	private String image;
	private String content;
	private long customerServiceId;
	private boolean fromUser;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCustomerServiceId() {
		return customerServiceId;
	}

	public void setCustomerServiceId(long customerServiceId) {
		this.customerServiceId = customerServiceId;
	}

	public boolean isFromUser() {
		return fromUser;
	}

	public void setFromUser(boolean fromUser) {
		this.fromUser = fromUser;
	}
}
