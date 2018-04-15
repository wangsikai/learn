package com.lanking.uxb.service.user.value;

import java.io.Serializable;

/**
 * 签到VO
 * 
 * @since
 * @author wangsenhao
 *
 */
public class VCheckIn implements Serializable {

	private static final long serialVersionUID = -6326880445627326915L;
	/**
	 * 今天已领积分
	 */
	private int point;
	/**
	 * 明天可领积分
	 */
	private int nextPoint;
	/**
	 * 今天是否签到
	 */
	private Boolean isCheckIn;

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getNextPoint() {
		return nextPoint;
	}

	public void setNextPoint(int nextPoint) {
		this.nextPoint = nextPoint;
	}

	public Boolean getIsCheckIn() {
		return isCheckIn;
	}

	public void setIsCheckIn(Boolean isCheckIn) {
		this.isCheckIn = isCheckIn;
	}

}
