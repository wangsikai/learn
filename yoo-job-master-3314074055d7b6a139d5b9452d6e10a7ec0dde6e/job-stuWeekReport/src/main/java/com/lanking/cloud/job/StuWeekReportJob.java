package com.lanking.cloud.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.job.stuWeekReport.service.TaskStuWeekReportService;

public class StuWeekReportJob implements SimpleJob {

	@Autowired
	private TaskStuWeekReportService reportService;

	private Logger logger = LoggerFactory.getLogger(StuWeekReportJob.class);

	@Override
	public void execute(final ShardingContext shardingContext) {
		int modVal = Integer.parseInt(shardingContext.getShardingParameter());
		logger.info("开始");
		reportService.statClassWeek(modVal);
		reportService.statWeek(modVal);
		logger.info("结束");
	}
}
