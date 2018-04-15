package com.lanking.uxb.service.activity.api;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02WeekPowerRank;
import com.lanking.uxb.service.activity.value.HolidayActivity02RetRankInfo;

/**
 * 假期活动02接口
 * 
 * @author qiuxue.jiang
 *
 */
public interface HolidayActivity02WeekPowerRankService {
	/**
	 * 创建一条总周新增战力记录
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	void addActivity02WeekPowerRank(long activityCode, long userId,Integer power,Date startTime,Date endTime);
	
	/**
	 * 更新一条周新增战力排名记录
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	void updateActivity02WeekPowerRank(HolidayActivity02WeekPowerRank holidayActivity02WeekPowerRank);
	
	/**
	 * 查询一条周新增战力记录
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	HolidayActivity02WeekPowerRank getActivity02WeekPowerRank(long activityCode, long userId);
	
	/**
	 * 所有的前10条周提升战力排名记录和自己的周提升战力排名
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	List<HolidayActivity02RetRankInfo> getActivity02WeekPowerRanks(long activityCode, long userId, HolidayActivity02RetRankInfo me);
	
	/**
	 * 更新用户的周战力值
	 * 
	 * @param code 活动code
	 * @param userId 用户id
	 * @param difference 获得的战力值
	 */
	void updateUserWeekPower(Long code, Long userId, Integer difference);
}
