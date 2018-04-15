package com.lanking.uxb.service.activity.api;

import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PKRecord;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02User;

/**
 * 假期活动02接口
 * 
 * @author qiuxue.jiang
 *
 */
public interface HolidayActivity02UserService {

	/**
	 * 创建活动用户
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	void addActivity02User(long activityCode, long userId,long activityUserCode);

	/**
	 * 判断该用户是否已参加活动
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	Boolean hasJoinActivity(long activityCode, long userId);
	
	/**
	 * 查询该用户的参赛信息
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	HolidayActivity02User getUserActivityInfo(long activityCode, long userId);
	
	
	/**
	 * 获取当前最大的用户编号
	 * 
	 * @param activityCode
	 *            活动代码
	*/
	Long getMaxUserCode(long activityCode);
	
	/**
	 * 更新活动用户的统计
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	void updateActivity02UserStats(long activityCode, long userId,Integer power,HolidayActivity02PKRecord record);

	/**
	 * 更新用户战力
	 * 
	 * @param code 活动code
	 * @param userId 用户id
	 * @param difference 获得的战力值
	 */
	void updateUserPower(Long code, Long userId, Integer difference);
}
