package com.lanking.uxb.service.activity.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PowerRank;
import com.lanking.uxb.service.activity.value.HolidayActivity02RetRankInfo;

/**
 * 假期活动02接口
 * 
 * @author qiuxue.jiang
 *
 */
public interface HolidayActivity02PowerRankService {
	/**
	 * 创建一条总战力排名记录
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	void addActivity02PowerRank(long activityCode, long userId,Integer power);
	
	/**
	 * 更新一条总战力排名记录
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	void updateActivity02PowerRank(HolidayActivity02PowerRank holidayActivity02PowerRank);
	
	/**
	 * 查询一条总战力排名记录
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	HolidayActivity02PowerRank getActivity02PowerRank(long activityCode, long userId);
	
	
	/**
	 * 所有的前100条总战力排名记录和自己的总战力排名
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	List<HolidayActivity02RetRankInfo> getActivity02PowerRanks(long activityCode, long userId, HolidayActivity02RetRankInfo me);
	
	/**
	 * 更新用户战力值
	 * 
	 * @param code 活动code
	 * @param userId 用户id
	 * @param difference 获得的战力值
	 */
	void updateUserPower(Long code, Long userId, Integer difference);
}
