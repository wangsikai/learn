package com.lanking.cloud.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.job.clearEventTrace.ClearEventTraceJobService;

import lombok.extern.slf4j.Slf4j;

/**
 * 删除job日志
 * 
 * @author peng.zhao
 * @version 2017年10月13日
 */
@Slf4j
public class ClearEventTraceJob implements SimpleJob {

	@Autowired
	private ClearEventTraceJobService service;

	@Override
	public void execute(ShardingContext shardingContext) {
		log.info("ClearEventTraceJob start.");
		int param = Integer.parseInt(shardingContext.getShardingParameter());
		String deleteDate = service.getDeleteDate(param);

		service.clearJobExecutionLog(deleteDate);
		service.clearJobStatusTraceLog(deleteDate);
		log.info("ClearEventTraceJob end.");
	}
}
