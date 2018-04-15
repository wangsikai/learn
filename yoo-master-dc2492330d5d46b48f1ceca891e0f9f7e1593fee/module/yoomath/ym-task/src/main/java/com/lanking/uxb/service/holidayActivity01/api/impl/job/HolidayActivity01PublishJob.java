package com.lanking.uxb.service.holidayActivity01.api.impl.job;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;
import com.lanking.uxb.service.holidayActivity01.api.TaskHolidayActivity01HomeWorkPublishService;
import com.lanking.uxb.service.holidayActivity01.api.TaskHolidayActivity01Service;

public class HolidayActivity01PublishJob implements SimpleJob {
	@Autowired
	private TaskHolidayActivity01HomeWorkPublishService holidayActivity01HomeWorkPublishService;

	@Autowired
	private TaskHolidayActivity01Service holidayActivity01Service;

	private static final Logger logger = LoggerFactory.getLogger(HolidayActivity01PublishJob.class);

	@Override
	public void execute(ShardingContext shardingContext) {

		logger.info("---HolidayActivity01PublishTask---start---");
		// 作业下发
		List<HolidayActivity01> activitys = holidayActivity01Service.listAllActivity();
		for (HolidayActivity01 activity : activitys) {
			holidayActivity01HomeWorkPublishService.publish(activity.getCode());
		}
		logger.info("---HolidayActivity01PublishTask---end---");

	}

}
