package com.lanking.cloud.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.job.correctUserDayStat.service.UserMonthStatService;

import lombok.extern.slf4j.Slf4j;

/**
 * 小悠快批用户月统计
 * 
 * @author peng.zhao
 * @version 2018-3-13
 */
@Slf4j
public class CorrectUserMonthStatJob implements SimpleJob {

	@Autowired
	private UserMonthStatService userMonthStatService;

	@Override
	public void execute(ShardingContext shardingContext) {
		log.info("CorrectUserMonthStatJob start.");

		userMonthStatService.doUserMonthStat();
		log.info("CorrectUserMonthStatJob end.");
	}

}
