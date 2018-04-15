package com.lanking.uxb.service.holidayActivity01.value;

import java.io.Serializable;

/**
 * 用户勋章VO
 * 
 * @author peng.zhao
 * @version 2018年1月19日
 */
public class VHolidayActivity02Medal implements Serializable {

	private static final long serialVersionUID = -5085047319736532790L;
	
	/**
	 * 主键
	 */
	private Long id;
	
	/**
	 * 活动代码
	 */
	private Long activityCode;
	
	/**
	 * 勋章级别（一共五级）
	 * 
	 * 1,"小试牛刀"; 2,"最强大脑"; 3,"众生膜拜"; 4,"技压群雄"; 5,"人生巅峰";
	 */
	private Integer level;
	
	/**
	 * 是否达到勋章领取条件 0:未达到1:已达到
	 */
	private Integer achieved;
	
	/**
	 * 是否领取过0:未领取1:已领取
	 */
	private Integer received;
	
	/**
	 * 进度
	 */
	private Integer progress;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getAchieved() {
		return achieved;
	}

	public void setAchieved(Integer achieved) {
		this.achieved = achieved;
	}

	public Integer getReceived() {
		return received;
	}

	public void setReceived(Integer received) {
		this.received = received;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}
}
