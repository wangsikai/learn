package com.lanking.uxb.service.syncOrder.api.impl.job;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.uxb.service.syncOrder.api.TaskSyncOrderStartService;

public class TaskSyncOrderOpenVipJob implements SimpleJob {
	@Autowired
	private TaskSyncOrderStartService syncOrderStartService;

	@Override
	public void execute(ShardingContext shardingContext) {
		syncOrderStartService.initSyncOrder(new Date());
	}

}
