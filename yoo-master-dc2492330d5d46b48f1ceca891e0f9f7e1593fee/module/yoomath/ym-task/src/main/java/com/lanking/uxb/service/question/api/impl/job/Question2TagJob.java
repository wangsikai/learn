package com.lanking.uxb.service.question.api.impl.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.uxb.service.question.api.Question2TagTaskService;

public class Question2TagJob implements SimpleJob {
	@Autowired
	private Question2TagTaskService question2TagTaskService;

	@Override
	public void execute(ShardingContext shardingContext) {
		question2TagTaskService.doTask();
	}

}
