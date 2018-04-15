package com.lanking.uxb.zycon.yooCorrect.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.intercomm.yoocorrect.client.CorrectConsoleConfigDatawayClient;
import com.lanking.intercomm.yoocorrect.dto.CorrectRewardType;

@RestController
@RequestMapping(value = "correct/config")
public class YooCorrectConfigController {

	@Autowired
	private CorrectConsoleConfigDatawayClient correctConsoleConfigDatawayClient;
	
	/**
	 * 查询配置详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value detail() {
		return correctConsoleConfigDatawayClient.getCorrectConfig();
	}
	
	/**
	 * 添加奖励费用
	 * 
	 * @return
	 */
	@RequestMapping(value = "addRewardConfig", method = { RequestMethod.POST, RequestMethod.GET })
	public Value addRewardConfig(Long id, CorrectRewardType correctRewardType,
			Integer correctQuestionCount, String fee) {
		return correctConsoleConfigDatawayClient.addRewardConfig(id, correctRewardType, correctQuestionCount, fee);
	}
	
	/**
	 * 删除奖励费用
	 * 
	 * @param id config主键
	 * @param sequence 奖励费用的排序
	 * @return
	 */
	@RequestMapping(value = "deleteRewardConfig", method = { RequestMethod.POST, RequestMethod.GET })
	public Value deleteRewardConfig(Long id, Integer sequence) {
		return correctConsoleConfigDatawayClient.deleteRewardConfig(id, sequence);
	}
	
	/**
	 * 修改批改费用
	 * 
	 * @param id config主键
	 * @param blankQuestionFee 填空题费用
	 * @param answerQuestionFee 解答题费用
	 * @return
	 */
	@RequestMapping(value = "updateFeeConfig", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateFeeConfig(Long id, String blankQuestionFee, String answerQuestionFee) {
		return correctConsoleConfigDatawayClient.updateFeeConfig(id, blankQuestionFee, answerQuestionFee);
	}
}
