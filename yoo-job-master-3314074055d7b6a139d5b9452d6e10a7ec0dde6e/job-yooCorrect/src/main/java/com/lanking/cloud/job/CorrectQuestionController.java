package com.lanking.cloud.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.job.CorrectUserTrustRank.service.CorrectUserTrustRankService;
import com.lanking.cloud.job.correctQuestionDistribute.service.DistributeCorrectQuestionService;
import com.lanking.cloud.job.correctQuestionFailRecord.service.CorrectQuestionFailRecordService;
import com.lanking.cloud.job.correctQuestionFailRecord.service.SyncCorrectQuestionService;
import com.lanking.cloud.sdk.value.Value;

/**
 * <p>Title:测试用</p>
 * <p>Description:测试用<p>
 * @author pengcheng.yu
 * @date 2018年3月7日
 * @since 小优快批
 */
@RestController
@RequestMapping(value ="yoo/correct")
public class CorrectQuestionController {

	@Autowired
	DistributeCorrectQuestionService distributeCorrectQuestionService;
	@Autowired
	SyncCorrectQuestionService syncCorrectQuestionService;
	
	@Autowired
	CorrectUserTrustRankService correctUserTrustRankService;
	@RequestMapping(value = "test", method = { RequestMethod.POST, RequestMethod.GET })
	public Value test(){
		distributeCorrectQuestionService.distributeCorrectQuestions();
		return new Value();
	}
	
	@RequestMapping(value = "clear", method = { RequestMethod.POST, RequestMethod.GET })
	public Value clear(){
		distributeCorrectQuestionService.clearAbnormalCorrectQuestions();
		return new Value();
	}
	
	@RequestMapping(value = "check", method = { RequestMethod.POST, RequestMethod.GET })
	public Value check(){
		distributeCorrectQuestionService.checkTimeoutCorrectQuestionInfos();
		return new Value();
	}
}
