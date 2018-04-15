package com.lanking.cloud.domain.yoo.order.questionRecord;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.domain.yoo.order.OrderBaseInfo;

/**
 * 用户请求录题或者录教辅图书工单
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "question_record_order")
public class QuestionRecordOrder extends OrderBaseInfo {

	private static final long serialVersionUID = 6588739587396512797L;

	/**
	 * 教材版本代码
	 */
	@Column(name = "category_code")
	private Integer categoryCode;

	/**
	 * 免费录题数量
	 */
	@Column(name = "record_count")
	private Long recordCount;

	/**
	 * 已录题数量
	 */
	@Column(name = "question_count")
	private Long questionCount;

	/**
	 * 请求类型
	 */
	@Column(name = "type", precision = 3)
	private QuestionRecordOrderType type;

	/**
	 * 订单状态
	 */
	@Column(name = "order_status", precision = 3)
	private QuestionRecordOrderStatus orderStatus;

	/**
	 * 附件文件ID列表
	 */
	@Type(type = JSONType.TYPE)
	@Column(name = "attach_files", length = 256)
	private List<Long> attachFiles;

	/**
	 * 用户手机号
	 */
	@Column(name = "mobile", length = 20)
	private String mobile;

	/**
	 * 录入描述
	 */
	@Column(name = "description", length = 2000)
	private String description;

	/**
	 * 用户留言(联系不上)
	 */
	@Column(name = "message", length = 500)
	private String message;

	/**
	 * 后台管理未联系上留言日期
	 */
	@Column(name = "update_message_at", columnDefinition = "datetime(3)")
	private Date updateMessageAt;

	/**
	 * 后台关闭需求留言
	 */
	@Column(name = "close_message", length = 500)
	private String closeMessage;

	/**
	 * 后台关闭需求日期
	 */
	@Column(name = "close_at", columnDefinition = "datetime(3)")
	private Date closeAt;

	public Long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}

	public QuestionRecordOrderType getType() {
		return type;
	}

	public void setType(QuestionRecordOrderType type) {
		this.type = type;
	}

	public QuestionRecordOrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(QuestionRecordOrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<Long> getAttachFiles() {
		return attachFiles;
	}

	public void setAttachFiles(List<Long> attachFiles) {
		this.attachFiles = attachFiles;
	}

	public Integer getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(Integer categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Long questionCount) {
		this.questionCount = questionCount;
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
