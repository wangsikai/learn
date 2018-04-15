package com.lanking.uxb.service.fallible.api.impl.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.uxb.service.fallible.api.TaskDoQuestionStuStatService;

public class DoQuestionStuStatJob implements SimpleJob {
	@Autowired
	private TaskDoQuestionStuStatService taskDoQuestionStuStatService;

	@Override
	public void execute(ShardingContext shardingContext) {
		taskDoQuestionStuStatService.statStuDoQuestion();
		taskDoQuestionStuStatService.statStuDoQuestionKp();
	}

}
