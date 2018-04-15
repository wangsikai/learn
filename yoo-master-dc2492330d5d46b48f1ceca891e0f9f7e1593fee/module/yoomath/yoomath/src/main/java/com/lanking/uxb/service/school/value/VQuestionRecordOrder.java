package com.lanking.uxb.service.school.value;

import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrderStatus;
import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrderType;
import com.lanking.uxb.service.file.value.VFile;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Value for QuestionRecordOrder
 *
 * @author xinyu.zhou
 * @since 2.6.0
 */
public class VQuestionRecordOrder implements Serializable {
	private static final long serialVersionUID = 3002093121520750122L;
	private long id;
	private Date orderAt;
	private QuestionRecordOrderType type;
	private String description;
	private QuestionRecordOrderStatus status;
	private String message;
	private List<VFile> attachFiles;
	private String mobile;
	private Date updateAt;
	private Date updateMessageAt;
	private String closeMessage;
	private Date closeAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getOrderAt() {
		return orderAt;
	}

	public void setOrderAt(Date orderAt) {
		this.orderAt = orderAt;
	}

	public QuestionRecordOrderType getType() {
		return type;
	}

	public void setType(QuestionRecordOrderType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public QuestionRecordOrderStatus getStatus() {
		return status;
	}

	public void setStatus(QuestionRecordOrderStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<VFile> getAttachFiles() {
		return attachFiles;
	}

	public void setAttachFiles(List<VFile> attachFiles) {
		this.attachFiles = attachFiles;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Date getUpdateMessageAt() {
		return updateMessageAt;
	}

	public void setUpdateMessageAt(Date updateMessageAt) {
		this.updateMessageAt = updateMessageAt;
	}

	public String getCloseMessage() {
		return closeMessage;
	}

	public void setCloseMessage(String closeMessage) {
		this.closeMessage = closeMessage;
	}

	public Date getCloseAt() {
		return closeAt;
	}

	public void setCloseAt(Date closeAt) {
		this.closeAt = closeAt;
	}
}
