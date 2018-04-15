package com.lanking.uxb.service.holidayActivity01.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.holidayActivity01.api.TaskHolidayActivity01RateStatService;
import com.lanking.uxb.service.holidayActivity01.api.TaskHolidayActivity01StatisticsService;

@Service
public class TaskHolidayActivity01RateStatServiceImpl implements TaskHolidayActivity01RateStatService {

	private static final int FETCH_SIZE = 50;

	@Autowired
	private TaskHolidayActivity01StatisticsService taskHolidayActivity01StatisticsService;

	@Override
	public void statRate(HolidayActivity01 activity) {
		// 定位时间段
		List<List<Long>> periods = activity.getCfg().getPeriods();
		if (CollectionUtils.isEmpty(periods)) {
			return;
		}
		// 开始时间(活动开始)
		Date startDate = new Date(periods.get(0).get(0));
		// 结束时间
		Date endTDate = new Date(periods.get(periods.size() - 1).get(1));

		CursorPage<Long, Map> allData = taskHolidayActivity01StatisticsService
				.statisticClazzs(CP.cursor(Long.MIN_VALUE, FETCH_SIZE), startDate, endTDate, activity.getCode());
		// 删除
		taskHolidayActivity01StatisticsService.deleteStatistics(activity.getCode(), startDate, endTDate);
		while (allData.isNotEmpty()) {
			// 练习id
			taskHolidayActivity01StatisticsService.createStatistics(allData.getItems(), null, null, startDate,
					endTDate);
			// 获取下一页
			allData = taskHolidayActivity01StatisticsService.statisticClazzs(
					CP.cursor(allData.getNextCursor(), FETCH_SIZE), startDate, endTDate, activity.getCode());
		}
		Date nowTime = new Date();
		Date lastStartPeriodDate = null;
		for (List<Long> period : periods) {
			Date startPeriodDate = new Date(period.get(0));
			Date endPeriodDate = new Date(period.get(1));
			if (nowTime.after(endPeriodDate) && taskHolidayActivity01StatisticsService
					.getStatisticData(activity.getCode(), startPeriodDate, endPeriodDate, null) == null) {
				CursorPage<Long, Map> data = taskHolidayActivity01StatisticsService.statisticClazzs(
						CP.cursor(Long.MIN_VALUE, FETCH_SIZE), startDate, endPeriodDate, activity.getCode());
				while (data.isNotEmpty()) {
					// 练习id
					taskHolidayActivity01StatisticsService.createStatistics(data.getItems(), lastStartPeriodDate,
							startPeriodDate, startPeriodDate, endPeriodDate);
					// 获取下一页
					data = taskHolidayActivity01StatisticsService.statisticClazzs(
							CP.cursor(data.getNextCursor(), FETCH_SIZE), startDate, endPeriodDate, activity.getCode());
				}
			}
			lastStartPeriodDate = new Date(period.get(0));
		}

	}

}
