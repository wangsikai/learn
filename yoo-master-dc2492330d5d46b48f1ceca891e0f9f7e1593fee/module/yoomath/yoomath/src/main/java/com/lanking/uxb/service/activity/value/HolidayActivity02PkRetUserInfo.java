package com.lanking.uxb.service.activity.value;

import java.io.Serializable;


public class HolidayActivity02PkRetUserInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1163978429793995295L;

	/**
	 * 用户名字
	 */
	private String name;

	/**
	 * 用户的头像
	 */
	private String avatarUrl;
	
	/**
	 * 用户总战力值
	 */
	private Integer totalPower;
	
	/**
	 * 用户的正确率
	 */
	private String rightRate;

	/**
	 * 用户的输赢结果  0 输  1 平  2 胜利
	 */
	private Integer result;
	
	/**
	 * 是否是vip
	 */
	private Integer vip;
	
	/**
	 * 本次pk获得的战力值
	 */
	private Integer power;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Integer getTotalPower() {
		return totalPower;
	}

	public void setTotalPower(Integer totalPower) {
		this.totalPower = totalPower;
	}

	public String getRightRate() {
		return rightRate;
	}

	public void setRightRate(String rightRate) {
		this.rightRate = rightRate;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Integer getVip() {
		return vip;
	}

	public void setVip(Integer vip) {
		this.vip = vip;
	}

	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}
	
}
