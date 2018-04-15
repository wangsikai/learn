package com.lanking.uxb.service.honor.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLogType;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLogType;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStarLog;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.api.UserUpLevelService;
import com.lanking.uxb.service.honor.api.UserLevelsService;
import com.lanking.uxb.service.honor.api.UserTaskStarLogService;

/**
 * @author xinyu.zhou
 * @since 4.0.0
 */
@Service
@Transactional(readOnly = true)
public class UserTaskStarLogServiceImpl implements UserTaskStarLogService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier(value = "UserTaskStarLogRepo")
	private Repo<UserTaskStarLog, Long> repo;
	@Autowired
	@Qualifier(value = "CoinsLogRepo")
	private Repo<CoinsLog, Long> coinsLogRepo;
	@Autowired
	@Qualifier(value = "GrowthLogRepo")
	private Repo<GrowthLog, Long> growthLogRepo;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private UserUpLevelService upLevelService;
	@Autowired
	private UserLevelsService userLevelsService;

	@Override
	public UserTaskStarLog query(Date date, long userId) {
		Params params = Params.param("date", date);
		params.put("userId", userId);
		return repo.find("$query", params).get();
	}

	@Override
	@Transactional
	public UserTaskStarLog update(long userId, int star, String content, Date date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		UserTaskStarLog starLog = null;
		try {
			starLog = query(format.parse(format.format(date)), userId);
		} catch (ParseException e) {
			logger.error("更新用户任务星级日志出错", e);
		}

		if (starLog == null) {
			starLog = new UserTaskStarLog();
			starLog.setStar(star);
			starLog.setUserId(userId);
		} else {
			starLog.setStar(starLog.getStar() + star);
		}

		starLog.setCreateAt(date);
		if (StringUtils.isNotBlank(content)) {
			starLog.setContent(content);
		}

		return repo.save(starLog);
	}

	@Override
	@Transactional
	public UserTaskStarLog earn(long userId, int star, String content, Date date, int coinsValue, int growthValue) {
		UserTaskStarLog log = update(userId, star, content, date);

		if (growthValue > 0) {
			GrowthLog growthLog = new GrowthLog();
			growthLog.setUserId(userId);
			growthLog.setCreateAt(new Date());
			growthLog.setBiz(Biz.NULL);
			growthLog.setGrowthValue(growthValue);
			growthLog.setBizId(0);
			growthLog.setRuleCode(101010008);
			growthLog.setType(GrowthLogType.USER_TASK);
			growthLog.setStatus(Status.ENABLED);
			growthLog.setP1(log.getId().toString());

			growthLogRepo.save(growthLog);
		}
		if (coinsValue > 0) {
			CoinsLog coinsLog = new CoinsLog();
			coinsLog.setUserId(userId);
			coinsLog.setCoinsValue(coinsValue);
			coinsLog.setStatus(Status.ENABLED);
			coinsLog.setRuleCode(101010008);
			coinsLog.setCreateAt(new Date());
			coinsLog.setBiz(Biz.NULL);
			coinsLog.setBizId(0L);
			coinsLog.setType(CoinsLogType.USER_TASK);
			coinsLog.setP1(log.getId().toString());

			coinsLogRepo.save(coinsLog);
		}

		UserHonor userHonor = userHonorService.getUserHonor(userId);
		if (userHonor == null) {
			userHonor = new UserHonor();
			userHonor.setUserId(userId);
			userHonor.setCoins(coinsValue);
			userHonor.setGrowth(growthValue);
			userHonor.setCreateAt(new Date());
			userHonor.setUpdateAt(userHonor.getCreateAt());

			int level = userLevelsService.getLevelByGrowthValue(userHonor.getGrowth());
			upLevelService.updateLevel(userId, level, false, userHonor);
		} else {
			userHonor.setUpdateAt(new Date());
			userHonor.setGrowth(userHonor.getGrowth() + growthValue);
			userHonor.setCoins(userHonor.getCoins() + coinsValue);

			int level = userLevelsService.getLevelByGrowthValue(userHonor.getGrowth());
			if (level > userHonor.getLevel()) {
				userHonor.setUpgrade(true);
				upLevelService.updateLevel(userId, level, true, userHonor);

			} else {
				upLevelService.updateLevel(userId, level, false, userHonor);
			}
			userHonor.setLevel(level);
		}

		userHonorService.save(userHonor);
		return log;
	}
}
