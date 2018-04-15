package com.lanking.uxb.channelSales.channel.value;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 我的渠道--班级数据--作业列表VO
 * 
 * @author wangsenhao
 *
 */
public class VHomework {
	private String name;
	private Date startAt;
	private Date endAt;
	// 作业提交数
	private Long commitCount;
	// 作业分发数
	private Long distributeCount;
	// 作业状态
	private String statusName;
	private BigDecimal rightRate;
	// 假期作业--完成率
	private BigDecimal completionRate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Long getCommitCount() {
		return commitCount;
	}

	public void setCommitCount(Long commitCount) {
		this.commitCount = commitCount;
	}

	public Long getDistributeCount() {
		return distributeCount;
	}

	public void setDistributeCount(Long distributeCount) {
		this.distributeCount = distributeCount;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
	}

}
