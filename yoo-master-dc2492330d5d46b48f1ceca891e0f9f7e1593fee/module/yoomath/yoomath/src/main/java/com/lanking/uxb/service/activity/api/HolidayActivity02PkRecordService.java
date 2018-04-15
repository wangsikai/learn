package com.lanking.uxb.service.activity.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PKRecord;

/**
 * 假期活动02接口
 * 
 * @author qiuxue.jiang
 *
 */
public interface HolidayActivity02PkRecordService {

	/**
	 * 保存pk记录
	 * 
	 */
	HolidayActivity02PKRecord addPkRecord(Boolean history,HolidayActivity02PKRecord userRecord,HolidayActivity02PKRecord pkUserRecord);

	/**
	 * 获取一个用户的所有pk记录
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	List<HolidayActivity02PKRecord> listAllPkRecord(long activityCode, long userId);
	
	/**
	 * 随机获取一个不等于该用户的pk记录
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param userId
	 *            用户ID
	 */
	HolidayActivity02PKRecord getARandomPkRecord(long activityCode, long userId);
	
	/**
	 * 获取一个pk记录
	 * 
	 * @param pkId
	 *            pk记录的Id
	 */
	HolidayActivity02PKRecord get(long pkId);

	/**
	 * 查询指定数量的用户pk记录
	 */
	@SuppressWarnings("rawtypes")
	List<Map> listAllPkRecord(Long code, Long userId, Integer size);
}
