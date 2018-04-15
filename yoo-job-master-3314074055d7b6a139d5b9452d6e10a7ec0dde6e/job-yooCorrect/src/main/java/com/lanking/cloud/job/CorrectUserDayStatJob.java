package com.lanking.cloud.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.job.correctUserDayStat.service.UserDayStatService;

import lombok.extern.slf4j.Slf4j;

/**
 * 小悠快批日统计
 * 
 * @author peng.zhao
 * @version 2018-3-13
 */
@Slf4j
public class CorrectUserDayStatJob implements SimpleJob {

	@Autowired
	private UserDayStatService userDayStatService;
	
	@Override
	public void execute(ShardingContext shardingContext) {
		log.info("CorrectUserDayStatJob start.");
		
		userDayStatService.doUserDayStat();
		log.info("CorrectUserDayStatJob end.");
	}

}
