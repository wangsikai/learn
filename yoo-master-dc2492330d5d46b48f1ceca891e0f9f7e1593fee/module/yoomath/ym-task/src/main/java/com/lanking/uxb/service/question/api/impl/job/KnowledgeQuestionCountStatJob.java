package com.lanking.uxb.service.question.api.impl.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.uxb.service.question.api.KnowledgeQuestionCountStatService;
import com.lanking.uxb.service.question.api.TaskKnowledgeQuestionCountStatService;

public class KnowledgeQuestionCountStatJob implements SimpleJob {
	@Autowired
	private TaskKnowledgeQuestionCountStatService taskKnowledgeQuestionCountStatService;
	@Autowired
	private KnowledgeQuestionCountStatService knowledgeQuestionCountStatService;

	@Override
	public void execute(ShardingContext shardingContext) {
		// 删除历史统计
		knowledgeQuestionCountStatService.deleteKnowpoint();
		// 统计旧知识点
		taskKnowledgeQuestionCountStatService.countKnowledge(1);
		// 统计新知识点
		taskKnowledgeQuestionCountStatService.countKnowledge(2);
	}

}
