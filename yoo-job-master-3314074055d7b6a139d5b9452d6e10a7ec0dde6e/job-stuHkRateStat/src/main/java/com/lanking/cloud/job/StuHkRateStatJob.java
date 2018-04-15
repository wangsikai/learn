package com.lanking.cloud.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.job.stuHkRateStat.service.TaskStuHkRateStatService;

public class StuHkRateStatJob implements SimpleJob {

	@Autowired
	private TaskStuHkRateStatService statService;

	@Override
	public void execute(final ShardingContext shardingContext) {

		int modVal = Integer.parseInt(shardingContext.getShardingParameter());
		statService.taskStuHkRateStat(modVal);
	}
}
