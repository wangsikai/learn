package com.lanking.uxb.service.honor.api.impl.processor;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthRule;
import com.lanking.cloud.ex.core.NotImplementedException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthLogService;
import com.lanking.uxb.service.honor.api.GrowthProcessor;
import com.lanking.uxb.service.honor.api.GrowthRuleService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.api.UserLevelsService;
import com.lanking.uxb.service.honor.cache.GrowthCacheService;

public abstract class AbstractGrowthProcessor implements GrowthProcessor {
	@Autowired
	private GrowthCacheService growthCacheService;
	@Autowired
	private GrowthLogService growthLogService;
	@Autowired
	public GrowthRuleService growRuleService;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private UserLevelsService userLevelsService;
	@Autowired
	private CoinsService coninsService;

	private GrowthRule growthRule = null;

	@Override
	public boolean accpet(GrowthAction action) {
		return action == getAction();
	}

	@Override
	public GrowthAction getAction() {
		throw new NotImplementedException();
	}

	@Override
	public GrowthRule getGrowthRule() {
		if (growthRule == null) {
			growthRule = growRuleService.getByAction(getAction());
		}
		return growthRule;
	}

	@Override
	public GrowthLog process(GrowthLog growthLog, boolean isUpgrade) {
		throw new NotImplementedException();
	}

	public UserHonor checkLevel(UserHonor userHonor) {
		Integer level = userLevelsService.getLevelByGrowthValue(userHonor.getGrowth());
		int oldCoins = userHonor.getCoins();
		// 判断根据积分获取的等级 是不是目前用户对应的等级
		if (userHonor.getLevel() == level) {
			if (level == 1) {
				userHonor.setUpgrade(false);
			} else {
				userHonor.setUpgrade(userHonor.isUpgrade());
			}
		} else if (userHonor.getLevel() < level) {
			userHonor.setLevel(level);
			CoinsLog coinsLog = coninsService.earn(CoinsAction.GRADE_UP_REWARD, userHonor.getUserId());
			userHonor.setUpgrade(true);
			userHonor.setUpRewardCoins(coinsLog.getUpRewardCoins());
			if (oldCoins == userHonor.getCoins()) {
				userHonor.setCoins(userHonor.getCoins() + coinsLog.getUpRewardCoins());
			}
		}
		return userHonor;
	}

	public UserHonor update(GrowthLog growthLog) {
		UserHonor userHonor = userHonorService.getUserHonor(growthLog.getUserId());
		if (userHonor == null) {
			userHonor = new UserHonor();
			userHonor.setUserId(growthLog.getUserId());
			userHonor.setGrowth(growthLog.getGrowthValue());
			userHonor.setCreateAt(new Date());
			userHonor.setUpdateAt(userHonor.getCreateAt());
			userHonor = checkLevel(userHonor);
		} else {
			userHonor.setGrowth(userHonor.getGrowth() + growthLog.getGrowthValue());
			userHonor.setUpdateAt(new Date());
			userHonor = checkLevel(userHonor);
		}
		return userHonorService.save(userHonor);
	}

	public GrowthLog processOneTime(GrowthLog growthLog, boolean isUpgrade) {
		String oneTimeKey = growthCacheService.getOneTimeKey(getAction(), growthLog.getUserId());
		long count = growthCacheService.get(oneTimeKey);
		if (count == -1) {
			GrowthLog glog = growthLogService.find(getAction(), growthLog.getUserId());
			if (glog == null) {
				growthLog.setCreateAt(new Date());
				growthLog.setRuleCode(getGrowthRule().getCode());
				if (growthLog.getGrowthValue() == -1) {
					growthLog.setGrowthValue(getGrowthRule().getGrowthValue());
				}
				growthLog.setStatus(Status.ENABLED);
				// 之前没有记录
				growthLogService.save(growthLog);
				growthCacheService.incr(oneTimeKey);
				UserHonor userHonor = update(growthLog);
				if (isUpgrade) {
					growthLog.setHonor(userHonor);
				}
			} else {
				growthCacheService.incr(oneTimeKey);
			}
		}
		return growthLog;
	}
}
