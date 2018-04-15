package com.lanking.cloud.domain.yoo.activity.lottery;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 抽奖活动用户相关数据
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@MappedSuperclass
public class LotteryActivityUserKey implements Serializable {

	private static final long serialVersionUID = 6522342159324603419L;

	/**
	 * 对应的抽奖活动code
	 */
	@Id
	@Column(name = "activity_code", nullable = false)
	private Long activityCode;

	/**
	 * 用户ID
	 */
	@Id
	@Column(name = "user_id", nullable = false)
	private long userId;

	public LotteryActivityUserKey() {
	}

	public LotteryActivityUserKey(long activityCode, long userId) {
		this.activityCode = activityCode;
		this.userId = userId;
	}

	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
