package com.lanking.uxb.channelSales.channel.api;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 假期作业接口
 * 
 * @author wangsenhao
 *
 */
public interface CsHolidayHomeworkService {
	/**
	 * 获取班级对应的作业列表
	 * 
	 * @param classId
	 * @return
	 */
	Page<HolidayHomework> query(CsHomeworkQuery query, Pageable p);
}
