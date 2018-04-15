package com.lanking.uxb.service.activity.api;

import java.util.Date;

import com.lanking.cloud.domain.yoo.user.UserAction;

/**
 * 活动用户获得虚拟币记录.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月24日
 */
public interface LotteryActivityUserVirtualCoinsLogService {

	/**
	 * 获得截止时间后的最新虚拟币个数.
	 * 
	 * @param activityCode
	 *            活动
	 * @param lastTime
	 *            上次截止时间
	 * @param userId
	 *            用户
	 * @return
	 */
	public long findNewCount(long activityCode, Date lastTime, long userId);

	/**
	 * 添加log.
	 * 
	 * @param activityCode
	 *            活动码
	 * @param userId
	 *            用户ID
	 * @param ruleCode
	 *            规则
	 * @param action
	 *            动作
	 * @param incrCoins
	 *            增加金币数
	 */
	public void addLog(long activityCode, long userId, int ruleCode, UserAction action, int incrCoins);
}
