package com.lanking.cloud.job.correctUserDayStat.service;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.sdk.data.CursorPage;

/**
 * 小悠快批日统计
 * 
 * @author peng.zhao
 * @version 2018-3-13
 */
public interface TaskUserDayStatService {

	/**
	 * 查询小悠快批系统用户
	 * 
	 * @param fetchCount
	 * @param cursor
	 */
	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> queryUserId(int fetchCount, long cursor);
	
	/**
	 * 执行统计
	 * 
	 * @param userIds 用户id
	 */
	void statUserCorrect(List<Long> userIds);
}
