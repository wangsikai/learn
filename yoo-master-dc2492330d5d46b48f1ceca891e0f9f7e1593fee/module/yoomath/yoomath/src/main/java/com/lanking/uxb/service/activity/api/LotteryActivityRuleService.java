package com.lanking.uxb.service.activity.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.lottery.LotteryActivityRule;

/**
 * 活动规则接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月24日
 */
public interface LotteryActivityRuleService {

	/**
	 * 获得活动的规则集合.
	 * 
	 * @param activityCode
	 *            活动CODE
	 * @return
	 */
	List<LotteryActivityRule> listRulesByActivity(long activityCode);
}
