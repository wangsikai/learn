package com.lanking.uxb.service.honor.api;

import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;

/**
 * 用户成长值任务service
 * 
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 *
 * @version 2017年6月5日
 */
public interface UserUpLevelService {

	/**
	 * 根据用户成长值设置成就任务,web端调用自动完成任务,并更新用户荣誉
	 * 
	 * @param userId
	 *            用户
	 * @param growth
	 *            成长值
	 * @param coins
	 *            金币值
	 * @param form
	 *            更新growth和coinslog
	 * @return
	 */
	void setUserHonorWeb(long userId, Integer growth, Integer coins, UserTaskLogUpdateForm form);

	/**
	 * 客户端领取奖励触发
	 * 
	 * @param userId
	 *            用户
	 * @param level
	 *            等级
	 * @param reach
	 *            是否达成
	 * @param userHonor
	 *            荣誉
	 * @return
	 */
	void updateLevel(long userId, int level, boolean reach, UserHonor userHonor);

	/**
	 * 签到调用,不做直接领取
	 * 
	 * @param userId
	 *            用户
	 * @param growth
	 *            成长值
	 * @param coins
	 *            金币值
	 * @param form
	 *            更新growth和coinslog
	 * @return
	 */
	void setUserHonorSinged(long userId, Integer growth, Integer coins, UserTaskLogUpdateForm form);
}
