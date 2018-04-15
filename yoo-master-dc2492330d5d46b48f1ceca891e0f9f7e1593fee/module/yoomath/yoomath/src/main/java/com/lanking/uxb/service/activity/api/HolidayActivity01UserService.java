package com.lanking.uxb.service.activity.api;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01User;

/**
 * 假期活动01-参与活动的用户service.
 * 
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 *
 * @version 2017年6月14日
 */
public interface HolidayActivity01UserService {

	/**
	 * 通过活动code和用户id获得数据.
	 * 
	 * @param code
	 *            用户ID
	 * @param userId
	 *            用户ID
	 * @return
	 */
	HolidayActivity01User getByUserId(long code, long userId);

	/**
	 * 重置用户新增的抽奖次数.
	 * 
	 * @param userId
	 *            用户ID
	 */
	void resetUserNewLuckyDraw(long activityCode, long userId);

	/**
	 * 检查活动用户数据，若没有的话就创建.
	 * <p>
	 * 保证在用户增减抽奖次数等操作时，已经存在用户数据.
	 * </p>
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	void checkHolidayActivity01User(long activityCode, long userId);

	/**
	 * 增加/减少用户抽奖次数.
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 * @param count
	 *            增加/减少次数
	 * @param isPassive
	 *            是否为被动增加次数
	 */
	void addUserLuckyDraw(long activityCode, long userId, int count, boolean isPassive);
}
