package com.lanking.cloud.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.job.nationalDayActivity.service.TaskNationalDayActivity01TeaAwardService;

/**
 * 活动结束后统计老师得奖情况(值运算一次)
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月22日
 */
public class NationalDayActivity01TeaAwardJob implements SimpleJob {

	@Autowired
	private TaskNationalDayActivity01TeaAwardService service;

	@Override
	public void execute(ShardingContext shardingContext) {
		if (service.needAward()) {
			// 处理排行榜 教师榜取前10个
			service.statTeaAward(10);
		}
	}

}
