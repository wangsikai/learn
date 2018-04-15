package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.util.Date;

/**
 * 客服对话日志Value
 *
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
public class VYoomathCustomerServiceLog implements Serializable {

	private static final long serialVersionUID = -7222813905204162045L;

	private long userId;
	private long customerServiceId;
	private String content;
	private String image;
	private boolean fromUser;
	private Date createAt;
	private long id;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCustomerServiceId() {
		return customerServiceId;
	}

	public void setCustomerServiceId(long customerServiceId) {
		this.customerServiceId = customerServiceId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isFromUser() {
		return fromUser;
	}

	public void setFromUser(boolean fromUser) {
		this.fromUser = fromUser;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
