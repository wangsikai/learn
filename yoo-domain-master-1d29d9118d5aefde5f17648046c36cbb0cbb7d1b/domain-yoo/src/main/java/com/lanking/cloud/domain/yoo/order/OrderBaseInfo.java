package com.lanking.cloud.domain.yoo.order;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 工单订单基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public abstract class OrderBaseInfo implements Serializable {

	private static final long serialVersionUID = -6142671367799887798L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 订单编号
	 */
	@Column(name = "code", length = 128)
	private String code;

	/**
	 * 用户ID
	 * 
	 * <pre>
	 * 1.后台开通会员时,此字段不存储
	 * </pre>
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 下单时间
	 */
	@Column(name = "order_at", columnDefinition = "datetime(3)")
	private Date orderAt;

	/**
	 * 更新人ID
	 */
	@Column(name = "update_id")
	private Long updateId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 订单状态(用户自己的操作)
	 */
	@Column(name = "del_status", precision = 3, nullable = false)
	private Status delStatus = Status.ENABLED;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getOrderAt() {
		return orderAt;
	}

	public void setOrderAt(Date orderAt) {
		this.orderAt = orderAt;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
	}
}
