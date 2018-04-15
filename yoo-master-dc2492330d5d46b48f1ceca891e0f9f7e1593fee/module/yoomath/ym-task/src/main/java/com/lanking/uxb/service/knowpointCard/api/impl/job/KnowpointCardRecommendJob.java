package com.lanking.uxb.service.knowpointCard.api.impl.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.uxb.service.knowpointCard.api.KnowpointCardRecommendService;

public class KnowpointCardRecommendJob implements SimpleJob {
	@Autowired
	private KnowpointCardRecommendService knowpointCardRecommendService;

	@Override
	public void execute(ShardingContext shardingContext) {
		int page = 1;
		int size = 100;
		// 推送知识点卡片并且返回分页结果page
		Page<Long> knowpointCardPage = knowpointCardRecommendService.recommendKnowpointCard(page, size);
		// 继续推送page后续内容
		for (int index = 2; index <= knowpointCardPage.getPageCount(); index++) {
			knowpointCardRecommendService.recommendKnowpointCard(index, size);
		}

	}

}
