package com.lanking.uxb.service.honor.value;

import java.io.Serializable;
import java.util.List;

import com.lanking.cloud.domain.yoo.honor.UserLevels;

/**
 * 福利社的VO
 * 
 * @since yoomath V1.8
 * @author wangsenhao
 *
 */
public class VUserHonor implements Serializable {

	private static final long serialVersionUID = -1637189279081926484L;
	/**
	 * 成长值
	 */
	private int growth = 0;
	/**
	 * 金币值
	 */
	private int coins = 0;
	/**
	 * 当前等级
	 */
	private int level = 1;
	/**
	 * 是否已经签到
	 */
	private boolean checkIn = false;
	/**
	 * 升级需要成长值
	 */
	private int upNeedGrowth;
	/**
	 * 下一等级
	 */
	private int nextLevel = level + 1;
	/**
	 * 获取当前用户对应的等级情况<br>
	 * 1.由于我们的会员设定为12级。考虑到页面展示，到用户升级到6级之后，就以6级为起点，展示6-12级的进度<br>
	 * 2.考虑到后面积分可能会变，从数据库查询相应的信息
	 */
	private List<UserLevels> levels;
	/**
	 * 昨天是否签到过
	 */
	private boolean yesterdayCheckIn = false;

	private Long checkInCount;

	public int getGrowth() {
		return growth;
	}

	public void setGrowth(int growth) {
		this.growth = growth;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isCheckIn() {
		return checkIn;
	}

	public void setCheckIn(boolean checkIn) {
		this.checkIn = checkIn;
	}

	public int getUpNeedGrowth() {
		return upNeedGrowth;
	}

	public void setUpNeedGrowth(int upNeedGrowth) {
		this.upNeedGrowth = upNeedGrowth;
	}

	public int getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(int nextLevel) {
		this.nextLevel = nextLevel;
	}

	public List<UserLevels> getLevels() {
		return levels;
	}

	public void setLevels(List<UserLevels> levels) {
		this.levels = levels;
	}

	public boolean isYesterdayCheckIn() {
		return yesterdayCheckIn;
	}

	public void setYesterdayCheckIn(boolean yesterdayCheckIn) {
		this.yesterdayCheckIn = yesterdayCheckIn;
	}

	public Long getCheckInCount() {
		return checkInCount;
	}

	public void setCheckInCount(Long checkInCount) {
		this.checkInCount = checkInCount;
	}

}
