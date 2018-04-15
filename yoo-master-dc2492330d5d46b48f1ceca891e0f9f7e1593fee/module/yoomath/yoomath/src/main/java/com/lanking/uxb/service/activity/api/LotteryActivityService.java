package com.lanking.uxb.service.activity.api;

import com.lanking.cloud.domain.yoo.activity.lottery.LotteryActivity;

/**
 * 抽奖活动接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月22日
 */
public interface LotteryActivityService {

	LotteryActivity get(long code);

	void save(LotteryActivity lotteryActivity);
}
