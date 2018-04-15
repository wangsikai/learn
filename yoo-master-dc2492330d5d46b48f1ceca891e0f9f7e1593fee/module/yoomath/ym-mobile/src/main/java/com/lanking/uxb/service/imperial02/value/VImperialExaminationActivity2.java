package com.lanking.uxb.service.imperial02.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.lanking.uxb.service.imperial.value.VExaminationTime;

/**
 * 活动规则相关信息VO
 * 
 * @author peng.zhao
 * @version 2017年11月9日
 */
public class VImperialExaminationActivity2 implements Serializable {

	private static final long serialVersionUID = 7648210765995082886L;

	/**
	 * 报名开始时间
	 */
	private Date signUpStartTime;
	/**
	 * 报名结束时间
	 */
	private Date signUpEndTime;
	/**
	 * 活动开始时间
	 */
	private Date activityStartTime;
	/**
	 * 活动结束时间
	 */
	private Date activityEndTime;
	/**
	 * 活动时间表
	 */
	private List<VExaminationTime> timeList;

	public Date getSignUpStartTime() {
		return signUpStartTime;
	}

	public void setSignUpStartTime(Date signUpStartTime) {
		this.signUpStartTime = signUpStartTime;
	}

	public Date getSignUpEndTime() {
		return signUpEndTime;
	}

	public void setSignUpEndTime(Date signUpEndTime) {
		this.signUpEndTime = signUpEndTime;
	}

	public Date getActivityStartTime() {
		return activityStartTime;
	}

	public void setActivityStartTime(Date activityStartTime) {
		this.activityStartTime = activityStartTime;
	}

	public Date getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(Date activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	public List<VExaminationTime> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<VExaminationTime> timeList) {
		this.timeList = timeList;
	}
}
