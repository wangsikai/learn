package com.lanking.cloud.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.job.correctQuestionDistribute.service.DistributeCorrectQuestionService;

/**
 * <p>
 * Title:题目派发job
 * </p>
 * <p>
 * Description:小优快批服务,题目派发job
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月7日
 * @since 小优秀快批
 */
public class CorrectQuestionDistributeJob implements SimpleJob {

	@Autowired
	DistributeCorrectQuestionService distributeCorrectQuestionService;

	@Override
	public void execute(ShardingContext arg0) {
		distributeCorrectQuestionService.distributeCorrectQuestions();
		distributeCorrectQuestionService.checkTimeoutCorrectQuestionInfos();
		distributeCorrectQuestionService.clearAbnormalCorrectQuestions();
	}

}
