package com.lanking.uxb.zycon.qs.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrderStatus;
import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrderType;
import com.lanking.uxb.service.file.value.VFile;

/**
 * Value for QuestionRecordOrder
 *
 * @author xinyu.zhou
 * @since 2.6.0
 */
public class VZycQuestionRecordOrder implements Serializable {
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
	private Long recordCount;
	private Long questionCount;
	private String userName;
	private String schoolName;
	private String accountName;
	private String categoryName;
	private Date updateMessageAt;
	private Date closeAt;
	private String closeMessage;
	@JSONField(serialize = false)
	private Long schoolId;

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

	public Long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}

	public Long getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Long questionCount) {
		this.questionCount = questionCount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Date getUpdateMessageAt() {
		return updateMessageAt;
	}

	public void setUpdateMessageAt(Date updateMessageAt) {
		this.updateMessageAt = updateMessageAt;
	}

	public Date getCloseAt() {
		return closeAt;
	}

	public void setCloseAt(Date closeAt) {
		this.closeAt = closeAt;
	}

	public String getCloseMessage() {
		return closeMessage;
	}

	public void setCloseMessage(String closeMessage) {
		this.closeMessage = closeMessage;
	}
}
