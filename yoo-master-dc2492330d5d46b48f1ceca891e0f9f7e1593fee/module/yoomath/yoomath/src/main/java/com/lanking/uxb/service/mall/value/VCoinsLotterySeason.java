package com.lanking.uxb.service.mall.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 每期抽奖规则及名称
 * 
 * @see CoinsLotterySeason
 *
 * @author xinyu.zhou
 * @since 2.4.0
 */
public class VCoinsLotterySeason implements Serializable {
	private static final long serialVersionUID = -2952355713386096486L;

	private Long id;
	private Date startTime;
	private Date endTime;

	// 距离开始时间(millis) 若<=0表示已经开始
	private long remainTime;
	private int userJoinTimes;
	private Status status;
	private String title;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public long getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(long remainTime) {
		this.remainTime = remainTime;
	}

	public int getUserJoinTimes() {
		return userJoinTimes;
	}

	public void setUserJoinTimes(int userJoinTimes) {
		this.userJoinTimes = userJoinTimes;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
