package com.lanking.uxb.service.imperialExamination.api.data;


/**
 * 科举活动获奖记录辅助类
 * 
 * @author <a href="mailto:qiuxue.jiang@elanking.com">qiuxue.jiang</a>
 */
public class ImperialExaminationActivityAwardHelper {
	private long activityCode;

	private long userId;

	private long clazzId;

	/**
	 * 乡试得分
	 */
	private Integer provincialScore;
	
	/**
	 * 会试得分
	 */
	private Integer metropolitanScore;
	
	/**
	 * 殿试得分
	 */
	private Integer finalExamScore;
	
	/**
	 * 乡试用时
	 */
	private Integer provincialTime;
	
	/**
	 * 会试用时
	 */
	private Integer metropolitanTime;
	
	/**
	 * 殿试用时
	 */
	private Integer finalExamTime;

	/**
	 * 考场
	 */
	private Integer room;

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

	public long getClazzId() {
		return clazzId;
	}

	public void setClazzId(long clazzId) {
		this.clazzId = clazzId;
	}

	public Integer getProvincialScore() {
		return provincialScore;
	}

	public void setProvincialScore(Integer provincialScore) {
		this.provincialScore = provincialScore;
	}

	public Integer getMetropolitanScore() {
		return metropolitanScore;
	}

	public void setMetropolitanScore(Integer metropolitanScore) {
		this.metropolitanScore = metropolitanScore;
	}

	public Integer getFinalExamScore() {
		return finalExamScore;
	}

	public void setFinalExamScore(Integer finalExamScore) {
		this.finalExamScore = finalExamScore;
	}

	public Integer getProvincialTime() {
		return provincialTime;
	}

	public void setProvincialTime(Integer provincialTime) {
		this.provincialTime = provincialTime;
	}

	public Integer getMetropolitanTime() {
		return metropolitanTime;
	}

	public void setMetropolitanTime(Integer metropolitanTime) {
		this.metropolitanTime = metropolitanTime;
	}

	public Integer getFinalExamTime() {
		return finalExamTime;
	}

	public void setFinalExamTime(Integer finalExamTime) {
		this.finalExamTime = finalExamTime;
	}

	public Integer getRoom() {
		return room;
	}

	public void setRoom(Integer room) {
		this.room = room;
	}
	
}
