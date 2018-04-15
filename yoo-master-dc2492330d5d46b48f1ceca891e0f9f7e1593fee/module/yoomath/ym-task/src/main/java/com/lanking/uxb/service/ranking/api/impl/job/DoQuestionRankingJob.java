package com.lanking.uxb.service.ranking.api.impl.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.uxb.service.ranking.api.TaskDoQuestionRankingService;

public class DoQuestionRankingJob implements SimpleJob {
	@Autowired
	private TaskDoQuestionRankingService taskDoQuestionRankingService;

	@Override
	public void execute(ShardingContext shardingContext) {
		taskDoQuestionRankingService.staticDoQuestionStudentStat();
		taskDoQuestionRankingService.staticDoQuestionClassStat();
		taskDoQuestionRankingService.staticDoQuestionSchoolStat();
	}

}
