package com.lanking.uxb.service.activity.value;

import java.io.Serializable;


public class HolidayActivity02RetRankInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3480354663954747394L;

	/**
	 * 用户名字
	 */
	private String name;
	
	/**
	 * 用户id
	 */
	private Long userId;


	/**
	 * 用户的头像
	 */
	private String avatarUrl;
	
	/**
	 * 用户的参赛编号
	 */
	private Long userActivityCode;

	
	/**
	 * 用户的总战力值或周提升战力值
	 */
	private Long power;

	/**
	 * 用户名次
	 */
	private Integer rank;
	
	/**
	 * 是否是vip
	 */
	private Integer vip;
	
	
	/**
	 * 用户参赛场次
	 */
	private Integer total;


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getAvatarUrl() {
		return avatarUrl;
	}


	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}


	public Long getUserActivityCode() {
		return userActivityCode;
	}


	public void setUserActivityCode(Long userActivityCode) {
		this.userActivityCode = userActivityCode;
	}


	public Long getPower() {
		return power;
	}


	public void setPower(Long power) {
		this.power = power;
	}


	public Integer getRank() {
		return rank;
	}


	public void setRank(Integer rank) {
		this.rank = rank;
	}


	public Integer getVip() {
		return vip;
	}


	public void setVip(Integer vip) {
		this.vip = vip;
	}


	public Integer getTotal() {
		return total;
	}


	public void setTotal(Integer total) {
		this.total = total;
	}
	
}
