package com.lanking.intercomm.yoocorrect.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.intercomm.yoocorrect.dto.CorrectRewardType;
import com.lanking.intercomm.yoocorrect.dto.WithdrawStatus;

/**
 * 小悠快批-配置服务客户端.
 * 
 * @author peng.zhao
 * @version 2018-3-17
 */
@FeignClient("${correct-server.name}")
public interface CorrectConsoleConfigDatawayClient {

	/**
	 * 获取快批对应用户.
	 * 
	 * @param uxbUserId
	 *            UXB用户ID
	 * @return
	 */
	@RequestMapping(value = "/console/correctConfig/getCorrectConfig", method = { RequestMethod.POST })
	Value getCorrectConfig();

	/**
	 * 添加奖励费用
	 * 
	 * @param id
	 *            CorrectConfig主键
	 * @param correctRewardType
	 * @param correctQuestionCount
	 *            完成批改数量
	 * @param fee
	 *            奖励
	 * @return
	 */
	@RequestMapping(value = "/console/correctConfig/addRewardConfig", method = { RequestMethod.POST })
	Value addRewardConfig(@RequestParam("id") Long id,
			@RequestParam("correctRewardType") CorrectRewardType correctRewardType,
			@RequestParam("correctQuestionCount") Integer correctQuestionCount, @RequestParam("fee") String fee);

	/**
	 * 删除奖励费用
	 * 
	 * @param id
	 *            CorrectConfig主键
	 * @param correctRewardType
	 * @param correctQuestionCount
	 *            完成批改数量
	 * @param fee
	 *            奖励
	 * @return
	 */
	@RequestMapping(value = "/console/correctConfig/deleteRewardConfig", method = { RequestMethod.POST })
	Value deleteRewardConfig(@RequestParam("id") Long id, @RequestParam("sequence") Integer sequence);

	/**
	 * 删除奖励费用
	 * 
	 * @param id
	 *            CorrectConfig主键
	 * @param blankQuestionFee
	 * @param answerQuestionFee
	 * @return
	 */
	@RequestMapping(value = "/console/correctConfig/updateFeeConfig", method = { RequestMethod.POST })
	Value updateFeeConfig(@RequestParam("id") Long id, @RequestParam("blankQuestionFee") String blankQuestionFee,
			@RequestParam("answerQuestionFee") String answerQuestionFee);

	/**
	 * 批改财务统计列表
	 * 
	 * @param size
	 * @param page
	 * @param year
	 * @param month
	 * @return
	 */
	@RequestMapping(value = "/console/finaStat/getFinaStats", method = { RequestMethod.POST })
	Value getFinaStats(@RequestParam("size") Integer size, @RequestParam("page") Integer page,
			@RequestParam("year") Integer year, @RequestParam("month") Integer month);

	/**
	 * 批改财务统计列表
	 * 
	 * @param size
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/console/correctUserStat/userDayStats", method = { RequestMethod.POST })
	Value getUserDayStats(@RequestParam("size") Integer size, @RequestParam("page") Integer page);

	/**
	 * 取用户的月度统计信息
	 * 
	 * @param userId
	 *            小悠系统的userId
	 * @return
	 */
	@RequestMapping(value = "/console/correctUserStat/userMonthStats", method = { RequestMethod.POST })
	Value getUserMonthStats(@RequestParam("userId") Long userId);

	/**
	 * 批改财务统计列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/console/finaStat/getWithdrawApplys", method = { RequestMethod.POST })
	Value getWithdrawApplys(@RequestParam("size") Integer size, @RequestParam("page") Integer page,
			@RequestParam("year") Integer year, @RequestParam("month") Integer month,
			@RequestParam("correctWithdrawApplyId") Long correctWithdrawApplyId,
			@RequestParam("accountName") String accountName, @RequestParam("realName") String realName,
			@RequestParam("status") WithdrawStatus status);
	
	/**
	 * 取指定的用户信息
	 * 
	 * @param userId
	 *            uxb系统中的用户id
	 * @return
	 */
	@RequestMapping(value = "/console/correctUserStat/userInfo", method = { RequestMethod.POST })
	Value getUserInfo(@RequestParam("uxbUserIds") List<Long> uxbUserIds);
	
	/**
	 * 统计指定月份的用户统计信息
	 * 
	 * @param date
	 *            指定年月,格式yyyy-mm
	 * @return
	 */
	@RequestMapping(value = "/console/correctUserStat/userMonthStat", method = { RequestMethod.POST })
	Value userMonthStat(@RequestParam("year") Integer year, @RequestParam("month") Integer month);
}
