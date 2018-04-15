package com.lanking.uxb.rescon.statistics.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.user.UserType;

public interface UserStatisticsManage {

	/**
	 * 获取所有总的统计
	 * 
	 * @since 2.1
	 * @param vendorId
	 *            供应商ID
	 * @param userType
	 *            用户类型
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	List<Map> getAllDetail(long vendorId, UserType userType);

	/**
	 * 获取用户所有总的统计
	 * 
	 * @since 2.1
	 * @param userId
	 *            用户ID
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	Map getUserAllDetail(long userId);

	/**
	 * 查询按天的统计
	 * 
	 * @since 2.1
	 * @param vendorId
	 *            供应商ID
	 * @param userType
	 *            用户类型
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	List<Map> queryDayDetail(long vendorId, UserType userType, Date startTime, Date endTime);

	/**
	 * 查询用户按天的统计
	 * 
	 * @since 2.1
	 * @param userId
	 *            用户ID
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	List<Map> queryUserDayDetail(long userId, Date startTime, Date endTime);
}
