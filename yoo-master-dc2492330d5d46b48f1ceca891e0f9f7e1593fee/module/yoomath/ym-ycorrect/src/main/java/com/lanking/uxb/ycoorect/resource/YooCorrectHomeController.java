package com.lanking.uxb.ycoorect.resource;

import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.intercomm.yoocorrect.dto.CorrectAuthStatus;
import com.lanking.intercomm.yoocorrect.dto.CorrectConfigData;
import com.lanking.intercomm.yoocorrect.dto.CorrectHomePageData;
import com.lanking.intercomm.yoocorrect.dto.CorrectUserResponse;
import com.lanking.intercomm.yoocorrect.service.CorrectBillsDatawayService;
import com.lanking.intercomm.yoocorrect.service.CorrectHomeDatawayService;
import com.lanking.uxb.core.annotation.LoadCorrectUser;
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 首页接口.
 * 
 * @author wanlong.che
 *
 */
@RestController
@RequestMapping(value = "ycorrect/home")
public class YooCorrectHomeController {

	@Autowired
	private CorrectHomeDatawayService correctHomeDatawayService;

	@Autowired
	private CorrectBillsDatawayService correctBillsDatawayService;

	/**
	 * 首页数据.
	 * 
	 * @return
	 */
	@LoadCorrectUser
	@RequestMapping(value = "index")
	public Value index() {

		Map<String, Object> map = Maps.newHashMap();

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		// 获取其他数据
		CorrectHomePageData correctHomePageData = correctHomeDatawayService.getHomeData(correctUser.getId());

		boolean authenticated = (correctUser.getIdCardAuthStatus() == CorrectAuthStatus.PASS)
				&& (correctUser.getQualificationAuthStatus() == CorrectAuthStatus.PASS)
				&& StringUtils.isNotBlank(correctUser.getMobile());

		map.put("authenticated", authenticated); // 是否已全部认证
		map.put("trustRank", correctUser.getTrustRank()); // 信任值
		map.put("todayCorrect", correctHomePageData.getTodayCorrect()); // 今日批改题数
		map.put("todayEarn", correctHomePageData.getTodayEarn()); // 今日流水
		map.put("balance", correctUser.getBalance()); // 我的余额
		map.put("canWithdraw", correctHomePageData.isCanWithdraw()); // 今日是否可提现
		map.put("nextWithdrawDate", correctHomePageData.getNextWithdrawDate()); // 下个提现日
		map.put("withdrawBt", correctHomePageData.getWithdrawBt()); // 提现开始时间
		map.put("withdrawEt", correctHomePageData.getWithdrawEt()); // 提现结束时间
		map.put("isPassSimulation", correctUser.isPassSimulation()); // 是否模拟过
		map.put("todayRewards", correctHomePageData.getTodayRewards()); // 今日奖励数据

		return new Value(map);
	}

	/**
	 * 帮助页数据.
	 * 
	 * @return
	 */
	@RequestMapping(value = "help")
	public Value help() {

		// 获取配置数据
		CorrectConfigData configData = correctHomeDatawayService.getConfig();
		SimpleDateFormat formate = new SimpleDateFormat("HH:mm");

		Map<String, Object> map = Maps.newHashMap();
		map.put("defaultTrustRank", configData.getDefaultTrustRank()); // 初始信任值
		map.put("minTrustRank", configData.getMinTrustRank()); // 最低信任值
		map.put("blankQuestionFee", configData.getFeeConfig().getBlankQuestionFee()); // 批改费用-填空题
		map.put("answerQuestionFee", configData.getFeeConfig().getAnswerQuestionFee()); // 批改费用-解答题
		map.put("blankQuestionReduceRate", configData.getFeeConfig().getBlankQuestionReduceRate()); // 填空题错误惩罚扣减比例，例如3倍，此处为3
		map.put("answerQuestionReduceRate", configData.getFeeConfig().getAnswerQuestionReduceRate()); // 解答题错误惩罚扣减比例，例如3倍，此处为3
		map.put("withdrawWeekDay", configData.getWithdrawWeekDay()); // 提现周次
		map.put("withdrawBt", formate.format(configData.getWithdrawBt())); // 提现开始时间
		map.put("withdrawEt", formate.format(configData.getWithdrawEt())); // 提现结束时间
		map.put("dayWithdrawMax", configData.getDayWithdrawMax()); // 单日提现最大限额
		map.put("transferTimeComment", configData.getTransferTimeComment()); // 到账提示
		map.put("dayWithdrawCount", configData.getDayWithdrawCount()); // 单日提现次数

		return new Value(map);
	}
}
