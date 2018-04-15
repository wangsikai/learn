package com.lanking.cloud.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.job.nationalDayActivity.service.NationalDayActivity01StuAwardService;

/**
 * 活动结束后统计学生得奖情况(只运算一次)
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月22日
 */
public class NationalDayActivity01StuAwardJob implements SimpleJob {

	@Autowired
	@Qualifier("nda01StuAwardService")
	private NationalDayActivity01StuAwardService stuAwardService;

	@Override
	public void execute(ShardingContext shardingContext) {
		if (stuAwardService.needAward()) {
			stuAwardService.award();
		}
	}

}
