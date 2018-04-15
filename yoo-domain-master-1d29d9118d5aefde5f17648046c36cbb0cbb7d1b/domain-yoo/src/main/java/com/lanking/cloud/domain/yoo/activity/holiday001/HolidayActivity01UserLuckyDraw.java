package com.lanking.cloud.domain.yoo.activity.holiday001;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 假期活动01-参与活动的用户抽奖记录
 * 
 * <pre>
 * 1.”这个暑假，有我，由你”(2017年6月13日)
 * </pre>
 * 
 * @since 4.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年6月13日
 */
@Entity
@Table(name = "holiday_activity_01_user_luckydraw")
public class HolidayActivity01UserLuckyDraw implements Serializable {

	private static final long serialVersionUID = -7657127280971778217L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 活动代码
	 */
	@Column(name = "activity_code")
	private long activityCode;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 对应订单ID
	 */
	@Column(name = "order_id")
	private long orderId;

	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(long activityCode) {
		this.activityCode = activityCode;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
