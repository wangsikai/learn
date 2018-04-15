package com.lanking.uxb.service.imperial.value;

import java.util.Date;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;

/**
 * 用户看到的乡试、会试、殿试活动时间VO<br>
 * 报名页和活动页都需要
 * 
 * @author wangsenhao
 *
 */
public class VExaminationTime {

	private ImperialExaminationType type;
	private Date startTime;
	private Date endTime;
	// 星期几
	private String dayOfWeek1;
	private String dayOfWeek2;

	public ImperialExaminationType getType() {
		return type;
	}

	public void setType(ImperialExaminationType type) {
		this.type = type;
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

	public String getDayOfWeek1() {
		return dayOfWeek1;
	}

	public void setDayOfWeek1(String dayOfWeek1) {
		this.dayOfWeek1 = dayOfWeek1;
	}

	public String getDayOfWeek2() {
		return dayOfWeek2;
	}

	public void setDayOfWeek2(String dayOfWeek2) {
		this.dayOfWeek2 = dayOfWeek2;
	}

}
