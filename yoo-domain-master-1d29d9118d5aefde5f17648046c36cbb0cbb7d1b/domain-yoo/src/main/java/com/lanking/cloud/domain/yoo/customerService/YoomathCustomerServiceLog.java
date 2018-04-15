package com.lanking.cloud.domain.yoo.customerService;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 客服记录（相当于简单的聊天记录）
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "yoomath_customer_service_log")
public class YoomathCustomerServiceLog implements Serializable {

	private static final long serialVersionUID = 1821313728327474496L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 客服ID
	 */
	@Column(name = "customer_service_id")
	private long customerServiceId;

	/**
	 * 聊天内容
	 */
	@Column(length = 4000)
	private String content;

	/**
	 * 关联图片ID
	 */
	@Column(name = "img_id")
	private Long imgId;

	/**
	 * 用户发送标识
	 * 
	 * <pre>
	 * fromUser = true|from_user = 1,表示由用户发送
	 * fromUser = false|from_user = 0,表示由用户发送
	 * </pre>
	 * 
	 */
	@Column(name = "from_user")
	private boolean fromUser = false;

	/**
	 * 用户已读标识
	 */
	@Column(name = "user_read_status", precision = 3)
	private CustomerLogReadStatus userReadStatus = CustomerLogReadStatus.UNREAD;

	/**
	 * 客服已读标识
	 */
	@Column(name = "customer_read_status")
	private CustomerLogReadStatus customerReadStatus = CustomerLogReadStatus.UNREAD;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 此条记录状态
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Long getImgId() {
		return imgId;
	}

	public void setImgId(Long imgId) {
		this.imgId = imgId;
	}

	public boolean isFromUser() {
		return fromUser;
	}

	public void setFromUser(boolean fromUser) {
		this.fromUser = fromUser;
	}

	public CustomerLogReadStatus getUserReadStatus() {
		return userReadStatus;
	}

	public void setUserReadStatus(CustomerLogReadStatus userReadStatus) {
		this.userReadStatus = userReadStatus;
	}

	public CustomerLogReadStatus getCustomerReadStatus() {
		return customerReadStatus;
	}

	public void setCustomerReadStatus(CustomerLogReadStatus customerReadStatus) {
		this.customerReadStatus = customerReadStatus;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
