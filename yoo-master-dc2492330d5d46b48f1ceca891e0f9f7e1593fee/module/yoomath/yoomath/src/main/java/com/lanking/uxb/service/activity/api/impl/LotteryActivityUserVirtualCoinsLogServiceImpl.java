package com.lanking.uxb.service.activity.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.lottery.LotteryActivityUser;
import com.lanking.cloud.domain.yoo.activity.lottery.LotteryActivityUserVirtualCoinsLog;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.LotteryActivityUserService;
import com.lanking.uxb.service.activity.api.LotteryActivityUserVirtualCoinsLogService;

/**
 * 活动用户获得虚拟币记录接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月24日
 */
@Service
@Transactional(readOnly = true)
public class LotteryActivityUserVirtualCoinsLogServiceImpl implements LotteryActivityUserVirtualCoinsLogService {
	@Autowired
	@Qualifier("LotteryActivityUserVirtualCoinsLogRepo")
	private Repo<LotteryActivityUserVirtualCoinsLog, Long> repo;

	@Autowired
	private LotteryActivityUserService lotteryActivityUserService;

	@Override
	public long findNewCount(long activityCode, Date lastTime, long userId) {
		List<Long> objs = repo.find("$findNewCount",
				Params.param("activityCode", activityCode).put("lastTime", lastTime).put("userId", userId)).list(
				Long.class);
		if (objs.size() > 0 && objs.get(0) != null) {
			return (Long) objs.get(0);
		}
		return 0;
	}

	@Override
	@Transactional
	public void addLog(long activityCode, long userId, int ruleCode, UserAction action, int incrCoins) {
		LotteryActivityUserVirtualCoinsLog log = new LotteryActivityUserVirtualCoinsLog();
		log.setAction(action);
		log.setActivityCode(activityCode);
		log.setCreateAt(new Date());
		log.setIncrCoins(incrCoins);
		log.setRuleCode(ruleCode);
		log.setUserId(userId);
		repo.save(log);

		LotteryActivityUser lotteryActivityUser = lotteryActivityUserService.get(activityCode, userId);
		lotteryActivityUserService.updateTotalCoins(activityCode, userId, lotteryActivityUser == null ? incrCoins
				: lotteryActivityUser.getTotalCoins() + incrCoins);
	}
}
