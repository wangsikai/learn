package com.lanking.cloud.domain.yoo.member;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 用户会员信息基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public class UserMemberBaseInfo implements Serializable {

	private static final long serialVersionUID = -7734019409928991590L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 会员类型
	 */
	@Column(name = "member_type", precision = 3)
	private MemberType memberType;

	/**
	 * 包含（精确到日）
	 */
	@Column(name = "start_at", columnDefinition = "datetime(3)")
	private Date startAt;

	/**
	 * 包含（精确到日）
	 */
	@Column(name = "end_at", columnDefinition = "datetime(3)")
	private Date endAt;

	/**
	 * 用户购买、续费会员时对应的订单ID.
	 */
	@Column(name = "order_id")
	private Long orderID;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public Date getStartAt() {
		return startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public Long getOrderID() {
		return orderID;
	}

	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}

	@Transient
	public Date getStartTime() {
		Calendar startTime = Calendar.getInstance();
		startTime.setTime(getStartAt());
		startTime.set(Calendar.HOUR_OF_DAY, 0);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		return startTime.getTime();
	}

	@Transient
	public Date getEndTime() {
		Calendar endTime = Calendar.getInstance();
		endTime.setTime(getEndAt());
		endTime.set(Calendar.HOUR_OF_DAY, 23);
		endTime.set(Calendar.MINUTE, 59);
		endTime.set(Calendar.SECOND, 59);
		return endTime.getTime();
	}
}
