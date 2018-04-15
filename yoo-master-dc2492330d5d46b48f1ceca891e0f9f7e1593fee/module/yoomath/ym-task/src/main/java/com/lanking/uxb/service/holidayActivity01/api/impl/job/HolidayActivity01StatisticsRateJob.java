package com.lanking.uxb.service.holidayActivity01.api.impl.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;
import com.lanking.uxb.service.holidayActivity01.api.TaskHolidayActivity01RateStatService;
import com.lanking.uxb.service.holidayActivity01.api.TaskHolidayActivity01Service;

public class HolidayActivity01StatisticsRateJob implements SimpleJob {
	@Autowired
	private TaskHolidayActivity01RateStatService taskHolidayActivity01RateStatService;

	@Autowired
	private TaskHolidayActivity01Service holidayActivity01Service;

	@Override
	public void execute(ShardingContext shardingContext) {
		List<HolidayActivity01> activitys = holidayActivity01Service.listAllActivity();
		for (HolidayActivity01 activity : activitys) {
			taskHolidayActivity01RateStatService.statRate(activity);
		}
	}

}
