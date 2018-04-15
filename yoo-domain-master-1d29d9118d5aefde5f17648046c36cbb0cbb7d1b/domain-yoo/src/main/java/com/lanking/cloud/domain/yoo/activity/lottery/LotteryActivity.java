package com.lanking.cloud.domain.yoo.activity.lottery;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 抽奖活动
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "lottery_activity")
public class LotteryActivity implements Serializable {
	private static final long serialVersionUID = 6087348381714775149L;

	/**
	 * 活动代码
	 */
	@Id
	@Column(name = "code")
	private Long code;

	/**
	 * 活动名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 活动介绍
	 */
	@Column(name = "introduction", length = 1024)
	private String introduction;

	/**
	 * 活动创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 活动开始时间（包含）
	 */
	@Column(name = "start_time", columnDefinition = "datetime(3)")
	private Date startTime;

	/**
	 * 活动结束时间（包含）
	 */
	@Column(name = "end_time", columnDefinition = "datetime(3)")
	private Date endTime;

	/**
	 * 活动状态（注意查询时该活动状态与活动起止时间要联合判断）
	 */
	@Column(name = "status", precision = 3, nullable = false)
	private Status status = Status.ENABLED;

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
