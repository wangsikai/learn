package com.lanking.uxb.service.imperial.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationAwardType;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationCompositeIndex;

/**
 * 活动规则相关信息VO
 * 
 * @author wangsenhao
 *
 */
public class VImperialExaminationActivity implements Serializable {

	private static final long serialVersionUID = 7304614941198208060L;
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
	 * 奖品列表
	 */
	private List<ImperialExaminationAwardType> awardList;
	/**
	 * 评比标准
	 */
	private List<ImperialExaminationCompositeIndex> indexList;
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

	public List<ImperialExaminationAwardType> getAwardList() {
		return awardList;
	}

	public void setAwardList(List<ImperialExaminationAwardType> awardList) {
		this.awardList = awardList;
	}

	public List<ImperialExaminationCompositeIndex> getIndexList() {
		return indexList;
	}

	public void setIndexList(List<ImperialExaminationCompositeIndex> indexList) {
		this.indexList = indexList;
	}

	public List<VExaminationTime> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<VExaminationTime> timeList) {
		this.timeList = timeList;
	}

}
