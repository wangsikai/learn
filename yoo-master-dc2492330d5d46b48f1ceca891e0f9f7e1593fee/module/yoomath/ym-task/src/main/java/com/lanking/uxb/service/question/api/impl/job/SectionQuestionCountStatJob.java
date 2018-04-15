package com.lanking.uxb.service.question.api.impl.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.uxb.service.question.api.TaskSectionQuestionCountStatService;

public class SectionQuestionCountStatJob implements SimpleJob {
	@Autowired
	private TaskSectionQuestionCountStatService statService;

	@Override
	public void execute(ShardingContext shardingContext) {
		// 旧
		statService.chapterStat(1);
		// 新
		statService.chapterStat(2);
	}

}
