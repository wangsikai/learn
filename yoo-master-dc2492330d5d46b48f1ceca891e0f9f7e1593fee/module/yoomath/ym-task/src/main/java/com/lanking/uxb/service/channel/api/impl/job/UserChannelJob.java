package com.lanking.uxb.service.channel.api.impl.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.uxb.service.channel.api.QuestionSchoolUserCountService;
import com.lanking.uxb.service.channel.api.UserChannelCountService;

public class UserChannelJob implements SimpleJob {
	@Autowired
	private UserChannelCountService userChannelCountService;
	@Autowired
	private QuestionSchoolUserCountService questionSchoolUserCountService;

	@Override
	public void execute(ShardingContext shardingContext) {
		userChannelCountService.staticChannelUserCount();
		questionSchoolUserCountService.staticSchoolUserCount();
	}

}
