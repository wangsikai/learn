package com.lanking.uxb.service.honor.api.impl;

import java.util.ArrayList;
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
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.UserLevels;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLogType;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLogType;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.api.UserUpLevelService;
import com.lanking.uxb.service.honor.api.UserLevelsService;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;

@Service
@Transactional(readOnly = true)
public class UserUpLevelServiceImpl implements UserUpLevelService {

	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private UserLevelsService userLevelsService;
	@Autowired
	@Qualifier(value = "GrowthLogRepo")
	private Repo<GrowthLog, Long> growthLogRepo;
	@Autowired
	@Qualifier(value = "CoinsLogRepo")
	private Repo<CoinsLog, Long> coinsLogRepo;

	@Override
	@Transactional
	public void setUserHonorWeb(long userId, Integer growth, Integer coins, UserTaskLogUpdateForm form) {
		// 当前用户荣誉
		UserHonor userHonor = userHonorService.getUserHonor(userId);
		int level = 1;
		if (userHonor == null) {
			userHonor = new UserHonor();
			userHonor.setUserId(userId);
			userHonor.setCoins(coins);
			userHonor.setGrowth(growth);
			userHonor.setCreateAt(new Date());
			userHonor.setUpdateAt(userHonor.getCreateAt());
			level = userLevelsService.getLevelByGrowthValue(userHonor.getGrowth());
		} else {
			userHonor.setUpdateAt(new Date());
			userHonor.setGrowth(userHonor.getGrowth() + growth);
			userHonor.setCoins(userHonor.getCoins() + coins);
			// 完成任务后用户等级
			level = userLevelsService.getLevelByGrowthValue(userHonor.getGrowth());
		}

		// 达成任务条件
		if (level > userHonor.getLevel()) {
			userHonor.setUpgrade(true);
			UserLevels userLevels = userLevelsService.getUserLevel(level, Product.YOOMATH);
			userHonor.setLevel(level);
			int taskCode = getTaskCode(level);
			// 获取任务
			UserTask userTask = userTaskService.get(taskCode);
			UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();
			UserTaskLog log = userTaskLogService.findByCodeAndUser(taskCode, userId);
			UserTaskLogUpdateForm uForm = new UserTaskLogUpdateForm();
			if (log == null) {
				setUserLog(userId, userTask, cfg, userLevels, UserTaskLogStatus.RECEIVE);
				// 设置成就任务的荣誉
				userHonor.setGrowth(userHonor.getGrowth() + cfg.getGrowthValue());
				userHonor.setCoins(userHonor.getCoins() + cfg.getCoinsValue());

			} else {
				updateUserLog(log.getId(), UserTaskLogStatus.RECEIVE, true, userTask, userLevels.getMinGrowthValue(),
						userLevels.getMinGrowthValue());
				// 设置成就任务的荣誉
				userHonor.setGrowth(userHonor.getGrowth() + cfg.getGrowthValue());
				userHonor.setCoins(userHonor.getCoins() + cfg.getCoinsValue());
			}

			// 完成记录成长值和金币日志
			uForm.setUserId(userId);
			uForm.setTaskCode(taskCode);
			uForm.setGrowth(cfg.getGrowthValue());
			uForm.setCoins(cfg.getCoinsValue());
			uForm.setType(UserTaskType.ACHIEVEMENT);
			if (cfg.getGrowthValue() > 0) {
				updateGrowthLog(uForm);
			}
			if (cfg.getCoinsValue() > 0) {
				updateCoinsLog(uForm);
			}

			// 设置下一级任务
			setNextLevel(level, userId, userHonor.getGrowth());
		} else {
			if (level < 12) {
				userHonor.setLevel(level);
				int taskCode = getTaskCode(level + 1);
				// 获取任务
				UserTask userTask = userTaskService.get(taskCode);
				UserTaskLog log = userTaskLogService.findByCodeAndUser(taskCode, userId);
				if (log == null) {
					// 设置下一级任务
					setNextLevel(level, userId, userHonor.getGrowth());
				} else {
					UserLevels nextuserLevels = userLevelsService.getUserLevel(level + 1, Product.YOOMATH);
					updateUserLog(log.getId(), UserTaskLogStatus.TASKING, false, userTask,
							nextuserLevels.getMinGrowthValue(), userHonor.getGrowth());
				}
			}
		}

		userHonorService.save(userHonor);
		// 设置GrowthLog和CoinsLog
		updateGrowthLog(form);
		updateCoinsLog(form);
	}

	private int getTaskCode(int level) {
		switch (level) {
		case 2:
			return 101020001;
		case 3:
			return 101020002;
		case 4:
			return 101020003;
		case 5:
			return 101020004;
		case 6:
			return 101020005;
		case 7:
			return 101020006;
		case 8:
			return 101020007;
		case 9:
			return 101020008;
		case 10:
			return 101020009;
		case 11:
			return 101020010;
		case 12:
			return 101020011;
		}

		return 0;
	}

	/**
	 * 设置下一级任务
	 * 
	 * @param level
	 *            当前用户等级
	 * @param userId
	 * @param growth
	 *            成长值
	 */
	private void setNextLevel(int level, long userId, int growth) {
		if (level < 12) {
			int nexttaskCode = getTaskCode(level + 1);
			UserTaskLog log = userTaskLogService.findByCodeAndUser(nexttaskCode, userId);
			if (log == null) {
				UserTask nextuserTask = userTaskService.get(nexttaskCode);
				UserLevels nextLevels = userLevelsService.getUserLevel(level + 1, Product.YOOMATH);
				UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
				UserTaskRuleCfg cfg = nextuserTask.getUserTaskRuleCfg();

				form.setType(nextuserTask.getType());
				form.setUserId(userId);
				form.setStatus(UserTaskLogStatus.TASKING);
				form.setTaskCode(nextuserTask.getCode());
				form.setCoins(cfg.getCoinsValue());
				form.setGrowth(cfg.getGrowthValue());
				JSONObject contentJsonObj = new JSONObject();
				contentJsonObj.put("content", nextuserTask.getType().getName() + ":" + nextuserTask.getName());
				contentJsonObj.put("completeAt", System.currentTimeMillis());
				contentJsonObj.put("items", Collections.EMPTY_LIST);
				// 进度
				contentJsonObj.put("allCount", nextLevels.getMinGrowthValue());
				contentJsonObj.put("doCount", growth);

				form.setContent(contentJsonObj.toJSONString());
				form.setDate(new Date());

				userTaskLogService.update(form);
			}
		}
	}

	/**
	 * 设置用户奖励
	 */
	private void setUserLog(long userId, UserTask userTask, UserTaskRuleCfg cfg, UserLevels userLevels,
			UserTaskLogStatus status) {
		UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
		form.setType(userTask.getType());
		form.setUserId(userId);
		form.setStatus(status);
		form.setTaskCode(userTask.getCode());
		form.setCoins(cfg.getCoinsValue());
		form.setGrowth(cfg.getGrowthValue());
		JSONObject contentJsonObj = new JSONObject();
		contentJsonObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
		contentJsonObj.put("completeAt", System.currentTimeMillis());
		// 完成任务设置items
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		Map<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("index", 1);
		logMap.put("content", "完成1次");
		logMap.put("completeAt", System.currentTimeMillis());
		items.add(logMap);
		contentJsonObj.put("items", items);
		// 任务完成,进度设置成100
		contentJsonObj.put("allCount", userLevels.getMinGrowthValue());
		contentJsonObj.put("doCount", userLevels.getMinGrowthValue());

		form.setContent(contentJsonObj.toJSONString());
		form.setDate(new Date());

		userTaskLogService.update(form);
	}

	/**
	 * 更新用户奖励
	 */
	private void updateUserLog(long id, UserTaskLogStatus status, boolean reach, UserTask userTask, int allCount,
			int doCount) {
		UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
		form.setId(id);
		form.setStatus(status);
		JSONObject contentJsonObj = new JSONObject();
		contentJsonObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
		contentJsonObj.put("completeAt", System.currentTimeMillis());
		if (reach) {
			// 完成任务设置items
			List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("index", 1);
			logMap.put("content", "完成1次");
			logMap.put("completeAt", System.currentTimeMillis());
			items.add(logMap);
			contentJsonObj.put("items", items);
		} else {
			contentJsonObj.put("items", Collections.EMPTY_LIST);
		}
		// 任务完成,进度设置成100
		contentJsonObj.put("allCount", allCount);
		contentJsonObj.put("doCount", doCount);

		form.setContent(contentJsonObj.toJSONString());

		userTaskLogService.update(form);
	}

	@Override
	@Transactional
	public void updateLevel(long userId, int level, boolean reach, UserHonor userHonor) {
		// 当前用户荣誉
		// UserHonor userHonor = userHonorService.getUserHonor(userId);
		// 已完成任务
		if (reach) {
			int taskCode = getTaskCode(level);
			// 获取任务
			UserTask userTask = userTaskService.get(taskCode);
			UserTaskLog log = userTaskLogService.findByCodeAndUser(taskCode, userId);
			UserLevels userLevels = userLevelsService.getUserLevel(level, Product.YOOMATH);

			if (log == null) {
				UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();
				setUserLog(userId, userTask, cfg, userLevels, UserTaskLogStatus.COMPLETE);
			} else {
				updateUserLog(log.getId(), UserTaskLogStatus.COMPLETE, true, userTask, userLevels.getMinGrowthValue(),
						userHonor.getGrowth());
			}
			// 设置下一级任务
			setNextLevel(level, userId, userHonor.getGrowth());
		} else {
			// 任务未完成
			if (level < 12) {
				userHonor.setLevel(level);
				int taskCode = getTaskCode(level + 1);
				// 获取任务
				UserTask userTask = userTaskService.get(taskCode);
				UserTaskLog log = userTaskLogService.findByCodeAndUser(taskCode, userId);
				UserLevels userLevels = userLevelsService.getUserLevel(level + 1, Product.YOOMATH);
				if (log == null) {
					// 设置下一级任务
					setNextLevel(level, userId, userHonor.getGrowth());
				} else {
					updateUserLog(log.getId(), UserTaskLogStatus.TASKING, false, userTask,
							userLevels.getMinGrowthValue(), userHonor.getGrowth());
				}
			}
		}
	}

	private void updateGrowthLog(UserTaskLogUpdateForm form) {
		GrowthLog growthLog = new GrowthLog();
		growthLog.setUserId(form.getUserId());
		growthLog.setCreateAt(new Date());
		growthLog.setBiz(Biz.NULL);
		growthLog.setGrowthValue(form.getGrowth());
		growthLog.setBizId(0);
		growthLog.setRuleCode(form.getTaskCode());
		growthLog.setType(GrowthLogType.USER_TASK);
		growthLog.setStatus(Status.ENABLED);

		growthLogRepo.save(growthLog);
	}

	private void updateCoinsLog(UserTaskLogUpdateForm form) {
		CoinsLog coinsLog = new CoinsLog();
		coinsLog.setUserId(form.getUserId());
		coinsLog.setCoinsValue(form.getCoins());
		coinsLog.setStatus(Status.ENABLED);
		coinsLog.setRuleCode(form.getTaskCode());
		coinsLog.setCreateAt(new Date());
		coinsLog.setBiz(Biz.NULL);
		coinsLog.setBizId(0L);
		coinsLog.setType(CoinsLogType.USER_TASK);

		coinsLogRepo.save(coinsLog);
	}

	@Override
	@Transactional
	public void setUserHonorSinged(long userId, Integer growth, Integer coins, UserTaskLogUpdateForm form) {
		// 当前用户荣誉
		UserHonor userHonor = userHonorService.getUserHonor(userId);
		int level = 1;
		if (userHonor == null) {
			userHonor = new UserHonor();
			userHonor.setUserId(userId);
			userHonor.setCoins(coins);
			userHonor.setGrowth(growth);
			userHonor.setCreateAt(new Date());
			userHonor.setUpdateAt(userHonor.getCreateAt());
			level = userLevelsService.getLevelByGrowthValue(userHonor.getGrowth());
		} else {
			userHonor.setUpdateAt(new Date());
			userHonor.setGrowth(userHonor.getGrowth() + growth);
			userHonor.setCoins(userHonor.getCoins() + coins);
			// 完成任务后用户等级
			level = userLevelsService.getLevelByGrowthValue(userHonor.getGrowth());
		}

		// 达成任务条件
		if (level > userHonor.getLevel()) {
			UserLevels userLevels = userLevelsService.getUserLevel(level, Product.YOOMATH);
			userHonor.setLevel(level);
			int taskCode = getTaskCode(level);
			userHonor.setUpgrade(true);
			// 获取任务
			UserTask userTask = userTaskService.get(taskCode);
			UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();
			UserTaskLog log = userTaskLogService.findByCodeAndUser(taskCode, userId);
			if (log == null) {
				setUserLog(userId, userTask, cfg, userLevels, UserTaskLogStatus.COMPLETE);
			} else {
				updateUserLog(log.getId(), UserTaskLogStatus.COMPLETE, true, userTask, userLevels.getMinGrowthValue(),
						userLevels.getMinGrowthValue());
			}
			// 设置下一级任务
			setNextLevel(level, userId, userHonor.getGrowth());
		} else {
			if (level < 12) {
				userHonor.setLevel(level);
				int taskCode = getTaskCode(level + 1);
				// 获取任务
				UserTask userTask = userTaskService.get(taskCode);
				UserTaskLog log = userTaskLogService.findByCodeAndUser(taskCode, userId);
				if (log == null) {
					// 设置下一级任务
					setNextLevel(level, userId, userHonor.getGrowth());
				} else {
					UserLevels nextuserLevels = userLevelsService.getUserLevel(level + 1, Product.YOOMATH);
					updateUserLog(log.getId(), UserTaskLogStatus.TASKING, false, userTask,
							nextuserLevels.getMinGrowthValue(), userHonor.getGrowth());
				}
			}
		}

		userHonorService.save(userHonor);
		// 设置GrowthLog和CoinsLog
		updateGrowthLog(form);
		updateCoinsLog(form);
	}
}
