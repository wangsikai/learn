package com.lanking.uxb.service.honor.api.impl.processor;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsRule;
import com.lanking.cloud.ex.core.NotImplementedException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.CoinsProcessor;
import com.lanking.uxb.service.honor.api.CoinsRuleService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.api.UserLevelsService;
import com.lanking.uxb.service.honor.cache.CoinsCacheService;

public abstract class AbstractCoinsProcessor implements CoinsProcessor {
	@Autowired
	private CoinsCacheService colinsCacheService;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	public CoinsRuleService coinsRuleService;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private UserLevelsService userLevelsService;
	private CoinsRule coinsRule = null;

	@Override
	public boolean accpet(CoinsAction action) {
		return action == getAction();
	}

	@Override
	public CoinsAction getAction() {
		throw new NotImplementedException();
	}

	@Override
	public CoinsRule getCoinsRule() {
		if (coinsRule == null) {
			coinsRule = coinsRuleService.getByAction(getAction());
		}
		return coinsRule;
	}

	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		throw new NotImplementedException();
	}

	public UserHonor update(CoinsLog coinsLog) {
		UserHonor userHonor = userHonorService.getUserHonor(coinsLog.getUserId());
		if (userHonor == null) {
			userHonor = new UserHonor();
			userHonor.setUserId(coinsLog.getUserId());
			userHonor.setCoins(coinsLog.getCoinsValue());
			userHonor.setCreateAt(new Date());
			userHonor.setUpdateAt(userHonor.getCreateAt());
		} else {
			userHonor.setUpdateAt(new Date());
			userHonor.setCoins(userHonor.getCoins() + coinsLog.getCoinsValue());
		}
		return userHonorService.save(userHonor);
	}

	public UserHonor updateNoSave(CoinsLog coinsLog) {
		UserHonor userHonor = userHonorService.getUserHonor(coinsLog.getUserId());
		if (userHonor == null) {
			userHonor = new UserHonor();
			userHonor.setUserId(coinsLog.getUserId());
			userHonor.setCoins(coinsLog.getCoinsValue());
			userHonor.setCreateAt(new Date());
			userHonor.setUpdateAt(userHonor.getCreateAt());
		} else {
			userHonor.setUpdateAt(new Date());
			userHonor.setCoins(userHonor.getCoins() + coinsLog.getCoinsValue());
		}
		return userHonor;
	}

	public CoinsLog processOneTime(CoinsLog coinsLog) {
		String oneTimeKey = colinsCacheService.getOneTimeKey(getAction(), coinsLog.getUserId());
		long count = colinsCacheService.get(oneTimeKey);
		if (count == -1) {
			CoinsLog clog = coinsLogService.find(getAction(), coinsLog.getUserId());
			if (clog == null) {
				coinsLog.setCreateAt(new Date());
				coinsLog.setRuleCode(getCoinsRule().getCode());
				if (coinsLog.getCoinsValue() == -1) {
					coinsLog.setCoinsValue(getCoinsRule().getCoinsValue());
				}
				coinsLog.setStatus(Status.ENABLED);
				// 之前没有记录
				coinsLogService.save(coinsLog);
				colinsCacheService.incr(oneTimeKey);
				update(coinsLog);
			} else {
				colinsCacheService.incr(oneTimeKey);
			}
		}
		return coinsLog;
	}

}
