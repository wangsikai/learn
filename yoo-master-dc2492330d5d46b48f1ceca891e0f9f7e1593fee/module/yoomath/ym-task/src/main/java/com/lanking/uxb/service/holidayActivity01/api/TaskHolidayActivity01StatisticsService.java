package com.lanking.uxb.service.holidayActivity01.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Statistics;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 假期作业活动统计
 * 
 * @author zemin.song
 *
 */
public interface TaskHolidayActivity01StatisticsService {

	/**
	 * 统计数据查询分页
	 * 
	 * @param cursorPageable
	 * @return
	 */
	CursorPage<Long, Map> statisticClazzs(CursorPageable<Long> pageable, Date startPeriodDate, Date endPeriodDate,
			Long activityCode);

	/**
	 * 创建统计数据
	 * 
	 * @param startTime
	 *            活动开始时间
	 * @param lastStartPeriodTime
	 *            上一次开始时间
	 * @param startPeriodTime
	 *            阶段开始时间
	 * @param endPeriodTime
	 *            阶段结束时间
	 * @param cursorPageable
	 * @return
	 */
	public void createStatistics(List<Map> maps, Date lastStartPeriodDate, Date lastEndPeriodDate, Date startPeriodDate,
			Date endPeriodDate);

	/**
	 * 删除数据
	 * 
	 * @param startTime
	 *            活动开始时间
	 * @param endTime
	 *            活动结束时间
	 * @return
	 */
	public int deleteStatistics(Long activityCode, Date startDate, Date endDate);

	/**
	 * 查询当前班级统计
	 * 
	 * @param activityCode
	 *            活动code
	 * @param lastStartPeriodTime
	 *            上个阶段开始时间
	 * @param startPeriodTime
	 *            活动阶段开始
	 * @param clazzId
	 *            班级iD
	 * @param endPeriodTime活动阶段结束
	 * 
	 * @return
	 */
	public HolidayActivity01Statistics getStatisticData(Long activityCode, Date startPeriodTime, Date endPeriodTime,
			Long clazzId);

}
