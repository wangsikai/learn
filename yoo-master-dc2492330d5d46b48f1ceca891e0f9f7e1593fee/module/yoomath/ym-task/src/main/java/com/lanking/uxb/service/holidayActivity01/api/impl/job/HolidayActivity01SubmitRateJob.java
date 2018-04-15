package com.lanking.uxb.service.holidayActivity01.api.impl.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.uxb.service.holidayActivity01.api.TaskHolidayActivity01SubmitRateStatService;

public class HolidayActivity01SubmitRateJob implements SimpleJob {
	@Autowired
	private TaskHolidayActivity01SubmitRateStatService taskService;

	private static final Logger logger = LoggerFactory.getLogger(HolidayActivity01SubmitRateJob.class);

	@Override
	public void execute(ShardingContext shardingContext) {
		logger.info("---HolidayActivity01SubmitRateTask---start---");
		// 更新作业提交率
		taskService.updateHkSubmitRate();
		// 更新班级提交率
		taskService.updateClassSubmitRate();
		// 更新用户提交率
		taskService.updateClassUserSubmitRate();
		logger.info("---HolidayActivity01SubmitRateTask---end---");
	}

}
