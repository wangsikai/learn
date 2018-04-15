package com.lanking.cloud.job.correctQuestionFailRecord.service;

import java.math.BigDecimal;
import java.util.List;

import com.lanking.microservice.domain.yoocorrect.CorrectConfig;
import com.lanking.microservice.domain.yoocorrect.CorrectFeeConfig;
import com.lanking.microservice.domain.yoocorrect.CorrectRewardType;

/**
 * 批改配置接口
 * 
 * @author peng.zhao
 * @version 2018-3-12
 */
public interface CorrectConfigService {

	
	/**
	 * 获取配置信息，按list类型取第一条
	 * 
	 * @return
	 */
	CorrectConfig getCorrectConfigs();
	
}
