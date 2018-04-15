package com.lanking.uxb.service.temp.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLogType;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLogType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthLogService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.api.UserLevelsService;
import com.lanking.uxb.service.temp.api.CoinsGrowthLogHandle2Service;

@Transactional(readOnly = true)
@Service
public class CoinsGrowthLogHandle2ServiceImpl implements CoinsGrowthLogHandle2Service {
	@Autowired
	@Qualifier("CoinsLogRepo")
	private Repo<CoinsLog, Long> coinsLogRepo;
	@Autowired
	@Qualifier("GrowthLogRepo")
	private Repo<GrowthLog, Long> growthLogRepo;
	@Autowired
	@Qualifier("HomeworkClazzRepo")
	private Repo<HomeworkClazz, Long> clazzRepo;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coinsService;

	@Autowired
	private GrowthLogService growthLogService;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private UserLevelsService userLevelsService;

	@Transactional
	@Override
	public void save(HomeworkClazz clazz) {
		GrowthLog growthLog = new GrowthLog();
		growthLog.setBiz(Biz.CLASS);
		growthLog.setBizId(clazz.getId());
		growthLog.setCreateAt(new Date());
		growthLog.setGrowthValue(100);
		growthLog.setRuleCode(107);
		growthLog.setType(GrowthLogType.GROWTH_RULE);
		growthLog.setUserId(clazz.getTeacherId());
		growthLogRepo.save(growthLog);
		// 金币值历史表
		CoinsLog coinsLog = new CoinsLog();
		coinsLog.setBiz(Biz.CLASS);
		coinsLog.setBizId(clazz.getId());
		coinsLog.setCoinsValue(1000);
		coinsLog.setCreateAt(new Date());
		coinsLog.setRuleCode(107);
		coinsLog.setType(CoinsLogType.COINS_RULE);
		coinsLog.setUserId(clazz.getTeacherId());
		coinsLogRepo.save(coinsLog);
		update(growthLog);
		update(coinsLog);
	}

	public UserHonor checkLevel(UserHonor userHonor) {
		int oldCoins = userHonor.getCoins();
		Integer level = userLevelsService.getLevelByGrowthValue(userHonor.getGrowth());
		// 判断根据积分获取的等级 是不是目前用户对应的等级
		userHonor.setUpgrade(true);
		if (userHonor.getLevel() == level) {
			if (level == 1) {
				// userHonor.setUpgrade(false);
			} else {
				// userHonor.setUpgrade(userHonor.isUpgrade());
			}
		} else if (userHonor.getLevel() < level) {
			userHonor.setLevel(level);
			CoinsLog coinsLog = coinsService.earn(CoinsAction.GRADE_UP_REWARD, userHonor.getUserId());
			// userHonor.setUpgrade(true);
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

	public Long logCount(int ruleCode, Long userId, Biz biz, Long bizId) {
		Params params = Params.param("ruleCode", 107).put("userId", userId);
		if (biz != null) {
			params.put("biz", biz.getValue());
		}
		if (bizId != null) {
			params.put("bizId", bizId);
		}
		Long count = coinsLogRepo.find("$logCount", params).get(Long.class);
		return count;
	}

	@Override
	public List<HomeworkClazz> getClassList() {
		return clazzRepo.find("$findExceed20List").list();
	}
}
