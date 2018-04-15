package com.lanking.cloud.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.job.nationalDayActivity.service.TaskNationalDayActivity01TeaScoreService;

/**
 * 活动过程中统计老师综合得分
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月22日
 */
public class NationalDayActivity01TeaScoreJob implements SimpleJob {

	@Autowired
	private TaskNationalDayActivity01TeaScoreService service;

	@Override
	public void execute(ShardingContext shardingContext) {
		// 1.删除因删除作业导致的冗余数据
		service.deleteTeaData();
		// 2.统计homework表数据
		service.statTeaScore();
	}

}
