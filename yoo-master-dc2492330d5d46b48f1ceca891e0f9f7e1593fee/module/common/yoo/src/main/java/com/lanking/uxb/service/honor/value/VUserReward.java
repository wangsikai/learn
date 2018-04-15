package com.lanking.uxb.service.honor.value;

import java.io.Serializable;

/**
 * 用户行为在成长体系中获得的奖励VO
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月10日 上午9:21:24
 */
public class VUserReward implements Serializable {

	private static final long serialVersionUID = -383185152126579283L;
	private boolean isUpGrade;
	private Integer level;
	private Integer growthValue;
	private Integer coinsValue;
	private Integer starValue;
	private Integer order;
	private Integer upRewardCoins;
	// 连续签到
	private long checkInCount;
	private Integer memberExtraCoinsValue;
	private Integer memberExtraGrowthValue;
	/**
	 * 下一等级
	 */
	private int nextLevel;
	/**
	 * 升级需要成长值
	 */
	private int upNeedGrowth;

	public VUserReward() {
		super();
	}

	public VUserReward(boolean isUpGrade, Integer level, Integer growthValue, Integer coinsValue) {
		super();
		this.isUpGrade = isUpGrade;
		this.level = level;
		this.growthValue = growthValue;
		this.coinsValue = coinsValue;
	}

	public VUserReward(Integer upRewardCoins, boolean isUpGrade, Integer level, Integer growthValue,
			Integer coinsValue) {
		super();
		this.upRewardCoins = upRewardCoins;
		this.isUpGrade = isUpGrade;
		this.level = level;
		this.growthValue = growthValue;
		this.coinsValue = coinsValue;
	}

	public VUserReward(boolean isUpGrade, Integer level, Integer growthValue, Integer coinsValue, Integer order) {
		super();
		this.isUpGrade = isUpGrade;
		this.level = level;
		this.growthValue = growthValue;
		this.coinsValue = coinsValue;
		this.order = order;
	}

	public VUserReward(boolean isUpGrade, Integer level, Integer growthValue, Integer coinsValue, Integer order,
			Integer starValue) {
		super();
		this.isUpGrade = isUpGrade;
		this.level = level;
		this.growthValue = growthValue;
		this.coinsValue = coinsValue;
		this.order = order;
		this.starValue = starValue;
	}

	public boolean isUpGrade() {
		return isUpGrade;
	}

	public void setUpGrade(boolean isUpGrade) {
		this.isUpGrade = isUpGrade;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getGrowthValue() {
		return growthValue;
	}

	public void setGrowthValue(Integer growthValue) {
		this.growthValue = growthValue;
	}

	public Integer getCoinsValue() {
		return coinsValue;
	}

	public void setCoinsValue(Integer coinsValue) {
		this.coinsValue = coinsValue;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getUpRewardCoins() {
		return upRewardCoins;
	}

	public void setUpRewardCoins(Integer upRewardCoins) {
		this.upRewardCoins = upRewardCoins;
	}

	public long getCheckInCount() {
		return checkInCount;
	}

	public void setCheckInCount(long checkInCount) {
		this.checkInCount = checkInCount;
	}

	public Integer getStarValue() {
		return starValue;
	}

	public void setStarValue(Integer starValue) {
		this.starValue = starValue;
	}

	public Integer getMemberExtraCoinsValue() {
		return memberExtraCoinsValue;
	}

	public void setMemberExtraCoinsValue(Integer memberExtraCoinsValue) {
		this.memberExtraCoinsValue = memberExtraCoinsValue;
	}

	public Integer getMemberExtraGrowthValue() {
		return memberExtraGrowthValue;
	}

	public void setMemberExtraGrowthValue(Integer memberExtraGrowthValue) {
		this.memberExtraGrowthValue = memberExtraGrowthValue;
	}

	public int getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(int nextLevel) {
		this.nextLevel = nextLevel;
	}

	public int getUpNeedGrowth() {
		return upNeedGrowth;
	}

	public void setUpNeedGrowth(int upNeedGrowth) {
		this.upNeedGrowth = upNeedGrowth;
	}
}
