package com.lanking.uxb.service.activity.api;

import java.util.Date;

import com.lanking.cloud.domain.yoo.activity.lottery.LotteryActivityUser;

/**
 * 活动用户数据接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月23日
 */
public interface LotteryActivityUserService {

	/**
	 * 获得用户活动数据.
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 * @return
	 */
	LotteryActivityUser get(long activityCode, long userId);

	/**
	 * 保存用户活动数据.
	 * 
	 * @param lotteryActivityUser
	 */
	void updateTotalCoins(long activityCode, long userId, int totalCoins);

	/**
	 * 更新截止时间.
	 * 
	 * @param activityCode
	 * @param userId
	 * @param date
	 */
	void updateLastIncrRecordAt(long activityCode, long userId, Date date);

	/**
	 * 创建用户活动数据.
	 * 
	 * @param activityCode
	 * @param userId
	 * @param totalCoins
	 * @return
	 */
	LotteryActivityUser create(long activityCode, long userId, int totalCoins);
}
