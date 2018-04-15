package com.lanking.cloud.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.job.correctQuestionFailRecord.service.SyncCorrectQuestionService;

/**
 * <p>Description:同步批改传送失败的批改题目job<p>
 * @author pengcheng.yu
 * @date 2018年3月20日
 * @since 小优秀快批
 */
public class SyncCorrectQuestionJob implements SimpleJob{

	@Autowired
	SyncCorrectQuestionService syncCorrectQuestionService;
	
	@Override
	public void execute(ShardingContext arg0) {
		syncCorrectQuestionService.sync();
	}

}
