package com.lanking.uxb.service.honor.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLogType;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLogType;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskType;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskUserScope;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.api.UserLevelsService;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.api.UserTaskStarLogService;
import com.lanking.uxb.service.honor.api.UserUpLevelService;
import com.lanking.uxb.service.honor.ex.HonorException;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserService;

/**
 * @author xinyu.zhou
 * @since 4.0.0
 */
@Service
@Transactional(readOnly = true)
public class UserTaskLogServiceImpl implements UserTaskLogService {
	@Autowired
	@Qualifier(value = "UserTaskLogRepo")
	private Repo<UserTaskLog, Long> repo;
	@Autowired
	@Qualifier(value = "GrowthLogRepo")
	private Repo<GrowthLog, Long> growthLogRepo;
	@Autowired
	@Qualifier(value = "CoinsLogRepo")
	private Repo<CoinsLog, Long> coinsLogRepo;

	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private UserTaskStarLogService userTaskStarLogService;
	@Autowired
	private UserLevelsService userLevelsService;
	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserUpLevelService upLevelService;

	@Override
	public UserTaskLog findByCodeAndUser(int code, long userId) {
		Params params = Params.param("code", code);
		params.put("userId", userId);

		return repo.find("$findByCodeAndUser", params).get();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserTaskLog> findByCodeAndUser(Collection<Integer> codes, long userId) {
		if (CollectionUtils.isEmpty(codes)) {
			return Collections.EMPTY_LIST;
		}
		Params params = Params.param("codes", codes);
		params.put("userId", userId);

		return repo.find("$findByCodesAndUser", params).list();
	}

	@Override
	public UserTaskLog findByCodeAndUser(int code, long userId, Date date) {
		Params params = Params.param("code", code);
		params.put("userId", userId);
		params.put("date", date);
		return repo.find("$findByCodeAndUser", params).get();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Integer, UserTaskLog> query(Collection<Integer> codes, long userId) {
		Params params = Params.param("codes", codes);
		params.put("userId", userId);

		List<UserTaskLog> logs = repo.find("$query", params).list();
		if (CollectionUtils.isEmpty(logs)) {
			return Collections.EMPTY_MAP;
		}

		Map<Integer, UserTaskLog> logMap = new HashMap<Integer, UserTaskLog>(logs.size());
		for (UserTaskLog log : logs) {
			logMap.put(Integer.valueOf(log.getTaskCode()), log);
		}

		return logMap;
	}

	@Override
	public UserTaskLog get(long id) {
		return repo.get(id);
	}

	@Override
	public List<UserTaskLog> mgetList(Collection<Long> ids) {
		return repo.mgetList(ids);
	}

	@Override
	public Map<Long, UserTaskLog> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}

	@Override
	@Transactional
	public UserTaskLog update(UserTaskLogUpdateForm form) {
		UserTaskLog log = null;
		if (form.getId() != null && form.getId() > 0) {
			log = repo.get(form.getId());
		}

		if (log == null) {
			log = new UserTaskLog();
			log.setTaskCode(form.getTaskCode());
			log.setUserId(form.getUserId());
			log.setCreateAt(new Date());
		}

		if (form.getCoins() != null) {
			log.setCoins(form.getCoins());
		}
		if (StringUtils.isNotBlank(form.getContent())) {
			log.setContent(form.getContent());
		}
		if (form.getGrowth() != null) {
			log.setGrowth(form.getGrowth());
		}
		if (form.getStar() != null) {
			log.setStar(form.getStar());
		}
		if (form.getStatus() != null) {
			log.setStatus(form.getStatus());
			if (form.getStatus() == UserTaskLogStatus.RECEIVE) {
				log.setReceiveAt(new Date());
			}
		}
		if (form.getType() != null) {
			log.setTaskType(form.getType());
		}
		if (form.getDate() != null) {
			log.setCreateAt(form.getDate());
		}
		if (form.getCompleteAt() != null) {
			log.setCompleteAt(form.getCompleteAt());
		}

		return repo.save(log);
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public Map<String, Object> earn(long id) {
		UserTaskLog log = repo.get(id);
		if (null == log) {
			throw new HonorException(HonorException.HONOR_USERTASK_NOT_EXISTS);
		}

		UserTask userTask = userTaskService.get(log.getTaskCode());
		if (userTask.getStatus() != UserTaskStatus.OPEN) {
			throw new HonorException(HonorException.HONOR_USERTASK_NOT_EXISTS);
		}
		User user = userService.get(log.getUserId());
		if (user.getUserChannelCode() <= 10000 && userTask.getUserScope() == UserTaskUserScope.CHANNEL_USER) {
			throw new HonorException(HonorException.HONOR_USERTASK_NOT_EXISTS);
		}
		if (user.getUserChannelCode() > 10000 && userTask.getUserScope() == UserTaskUserScope.SELF_REGISTRATION) {
			throw new HonorException(HonorException.HONOR_USERTASK_NOT_EXISTS);
		}
		if (userTask.getUserType() != Security.getUserType()) {
			throw new HonorException(HonorException.HONOR_USERTASK_NOT_EXISTS);
		}
		if (log.getStatus() == UserTaskLogStatus.RECEIVE) {
			throw new HonorException(HonorException.HONOR_USERTASK_GETED_REWARD);
		}

		int coins = log.getCoins();
		int growth = log.getGrowth();
		int star = log.getStar();

		log.setStatus(UserTaskLogStatus.RECEIVE);

		String content = log.getContent();
		JSONObject contentJsonObj = null;
		if (StringUtils.isNotBlank(content)) {
			contentJsonObj = JSONObject.parseObject(content);
			contentJsonObj.put("receiveAt", System.currentTimeMillis());
			log.setContent(contentJsonObj.toJSONString());
		}

		repo.save(log);

		Map<String, Object> retMap = new HashMap<String, Object>(5);
		retMap.put("star", star);

		if (growth > 0) {
			GrowthLog growthLog = new GrowthLog();
			growthLog.setUserId(log.getUserId());
			growthLog.setCreateAt(new Date());
			growthLog.setBiz(Biz.NULL);
			growthLog.setGrowthValue(growth);
			growthLog.setBizId(0);
			growthLog.setRuleCode(log.getTaskCode());
			growthLog.setType(GrowthLogType.USER_TASK);
			growthLog.setStatus(Status.ENABLED);
			growthLog.setP1(log.getId().toString());

			growthLogRepo.save(growthLog);
			retMap.put("growthLog", growthLog);
		}
		if (coins > 0) {
			CoinsLog coinsLog = new CoinsLog();
			coinsLog.setUserId(log.getUserId());
			coinsLog.setCoinsValue(coins);
			coinsLog.setStatus(Status.ENABLED);
			coinsLog.setRuleCode(log.getTaskCode());
			coinsLog.setCreateAt(new Date());
			coinsLog.setBiz(Biz.NULL);
			coinsLog.setBizId(0L);
			coinsLog.setType(CoinsLogType.USER_TASK);
			coinsLog.setP1(log.getId().toString());

			coinsLogRepo.save(coinsLog);
			retMap.put("coinsLog", coinsLog);
		}
		if (star > 0) {
			userTaskStarLogService.update(log.getUserId(), star, null, new Date());
		}

		UserHonor userHonor = userHonorService.getUserHonor(log.getUserId());
		if (userHonor == null) {
			userHonor = new UserHonor();
			userHonor.setUserId(log.getUserId());
			userHonor.setCoins(coins);
			userHonor.setGrowth(growth);
			userHonor.setCreateAt(new Date());
			userHonor.setUpdateAt(userHonor.getCreateAt());

			int level = userLevelsService.getLevelByGrowthValue(userHonor.getGrowth());
			// 触发用户成就任务
			upLevelService.updateLevel(log.getUserId(), level, false, userHonor);
		} else {
			userHonor.setUpdateAt(new Date());
			userHonor.setGrowth(userHonor.getGrowth() + growth);
			userHonor.setCoins(userHonor.getCoins() + coins);

			// 修改等级溢出问题
			int level = userLevelsService.getLevelByGrowthValue(userHonor.getGrowth());
			if (level > userHonor.getLevel()) {
				// 升级弹框
				userHonor.setUpgrade(true);
				// 触发用户成就任务
				upLevelService.updateLevel(log.getUserId(), level, true, userHonor);
			} else {
				// 触发用户成就任务
				upLevelService.updateLevel(log.getUserId(), level, false, userHonor);
			}
			userHonor.setLevel(level);
		}

		userHonorService.save(userHonor);
		retMap.put("honor", userHonor);

		return retMap;
	}

	@Override
	public long count(long code, long userId) {
		return repo.find("$countLogByCode", Params.param("code", code).put("userId", userId)).count();
	}

	@Override
	public Long countNotReceiveTask(UserTaskType userTaskType, long userId) {
		return repo.find("$countNotReceiveTask", Params.param("type", userTaskType.getValue()).put("userId", userId))
				.get(Long.class);
	}

	@Override
	public Date getLatestCompleteDate(UserTaskType type, long userId) {
		Params params = Params.param("type", type.getValue()).put("userId", userId);
		UserTaskLog userTaskLog = repo.find("$getLatestCompleteDate", params).get();
		return userTaskLog == null ? null : userTaskLog.getCompleteAt();
	}

	@Override
	@Transactional
	public void receiveReward(UserTask userTask, long userId) {
		UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();
		if (cfg.getCoinsValue() > 0) {
			CoinsLog coinsLog = new CoinsLog();
			coinsLog.setUserId(userId);
			coinsLog.setCoinsValue(cfg.getCoinsValue());
			coinsLog.setStatus(Status.ENABLED);
			coinsLog.setRuleCode(userTask.getCode());
			coinsLog.setCreateAt(new Date());
			coinsLog.setBiz(Biz.NULL);
			coinsLog.setBizId(0L);
			coinsLog.setType(CoinsLogType.USER_TASK);

			coinsLogRepo.save(coinsLog);
		}
		if (cfg.getGrowthValue() > 0) {
			GrowthLog growthLog = new GrowthLog();
			growthLog.setUserId(userId);
			growthLog.setCreateAt(new Date());
			growthLog.setBiz(Biz.NULL);
			growthLog.setGrowthValue(cfg.getGrowthValue());
			growthLog.setBizId(0);
			growthLog.setRuleCode(userTask.getCode());
			growthLog.setType(GrowthLogType.USER_TASK);
			growthLog.setStatus(Status.ENABLED);

			growthLogRepo.save(growthLog);
		}

		UserHonor userHonor = userHonorService.getUserHonor(userId);
		if (userHonor == null) {
			userHonor = new UserHonor();
			userHonor.setUserId(userId);
			userHonor.setCoins(cfg.getCoinsValue());
			userHonor.setGrowth(cfg.getGrowthValue());
			userHonor.setCreateAt(new Date());
			userHonor.setUpdateAt(userHonor.getCreateAt());

			int level = userLevelsService.getLevelByGrowthValue(userHonor.getGrowth());
			upLevelService.updateLevel(userId, level, false, userHonor);
		} else {
			userHonor.setUpdateAt(new Date());
			userHonor.setGrowth(userHonor.getGrowth() + cfg.getGrowthValue());
			userHonor.setCoins(userHonor.getCoins() + cfg.getCoinsValue());

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

	}
}
