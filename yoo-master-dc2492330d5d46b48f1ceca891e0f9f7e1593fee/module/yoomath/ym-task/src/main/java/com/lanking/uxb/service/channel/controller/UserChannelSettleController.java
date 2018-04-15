package com.lanking.uxb.service.channel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.channel.api.QuestionSchoolUserCountService;
import com.lanking.uxb.service.channel.api.UserChannelCountService;
import com.lanking.uxb.service.channel.api.UserChannelSettleService;

/**
 * TODO 测试临时使用，测试完成后删除.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年2月28日
 */
@RestController
@RequestMapping(value = "task/userChannelSettle")
public class UserChannelSettleController {
	@Autowired
	private UserChannelSettleService userChannelSettleService;
	@Autowired
	private UserChannelCountService userChannelCountService;
	@Autowired
	private QuestionSchoolUserCountService questionSchoolUserCountService;


	/**
	 * 统计指定年月的数据.
	 * 
	 * @param year
	 *            指定年份
	 * @param month
	 *            指定月份，从1开始
	 * @return
	 */
	@RequestMapping(value = "test")
	public Value testChannelSettleTask(int year, int month) {
		userChannelSettleService.staticChannelSettle(year, month);
		return new Value();
	}

	@RequestMapping(value = "channel")
	public Value testChannelTask() {
		userChannelCountService.staticChannelUserCount();
		questionSchoolUserCountService.staticSchoolUserCount();
		return new Value();
	}
}
