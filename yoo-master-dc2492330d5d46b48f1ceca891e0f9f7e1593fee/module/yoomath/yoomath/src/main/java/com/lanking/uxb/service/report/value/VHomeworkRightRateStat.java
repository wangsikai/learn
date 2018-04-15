package com.lanking.uxb.service.report.value;

import java.io.Serializable;
import java.math.BigDecimal;

public class VHomeworkRightRateStat implements Serializable {

	private static final long serialVersionUID = -2862400505838492793L;

	/**
	 * 班级ID
	 */
	private Long homeworkClassId;

	/**
	 * 正确率
	 */
	private BigDecimal rightRate;

	/**
	 * 统计时间,存储精度到天,目前是存储触发统时间的年月日
	 */
	private String statisticsTime;

	public Long getHomeworkClassId() {
		return homeworkClassId;
	}

	public void setHomeworkClassId(Long homeworkClassId) {
		this.homeworkClassId = homeworkClassId;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public String getStatisticsTime() {
		return statisticsTime;
	}

	public void setStatisticsTime(String statisticsTime) {
		this.statisticsTime = statisticsTime;
	}

}
