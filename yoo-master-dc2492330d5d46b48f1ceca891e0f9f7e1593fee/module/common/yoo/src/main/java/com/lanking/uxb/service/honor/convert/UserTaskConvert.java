package com.lanking.uxb.service.honor.convert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskAchievementType;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.value.VUserTask;
import com.lanking.uxb.service.honor.value.VUserTaskLog;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * UserTask -> VUserTask
 *
 * @author xinyu.zhou
 * @since 4.0.0
 */
@Component
public class UserTaskConvert extends Converter<VUserTask, UserTask, Integer> {
	@Autowired
	private UserTaskLogConvert userTaskLogConvert;
	@Autowired
	private UserTaskLogService userTaskLogService;

	@Override
	protected Integer getId(UserTask userTask) {
		return userTask.getCode();
	}

	public VUserTask to(UserTask userTask, boolean initUserTaskLog) {
		userTask.setInitUserTaskLog(initUserTaskLog);
		return super.to(userTask);
	}

	public List<VUserTask> to(List<UserTask> userTasks, boolean initUserTaskLog) {
		for (UserTask u : userTasks) {
			u.setInitUserTaskLog(initUserTaskLog);
		}

		return super.to(userTasks);
	}

	@Override
	protected VUserTask convert(UserTask userTask) {
		VUserTask v = new VUserTask();
		v.setType(userTask.getType());
		v.setStatus(userTask.getStatus());
		v.setUserType(userTask.getUserType());
		v.setCode(userTask.getCode());
		v.setNote(userTask.getNote());
		v.setCoinsNote(userTask.getCoinsNote());
		v.setGrowthNote(userTask.getGrowthNote());
		v.setUserScope(userTask.getUserScope());
		v.setSequence(userTask.getSequence());
		v.setName(userTask.getName());

		UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();
		if (cfg.getGrowthValue() < 0) {
			if (cfg.getlGrowthValue() >= 0 && cfg.getrGrowthValue() >= 0) {
				v.setlGrowthValue(cfg.getlGrowthValue());
				v.setrGrowthValue(cfg.getrGrowthValue());
				v.setGrowthValueTitle(cfg.getlGrowthValue() + "~" + cfg.getrGrowthValue());
			}
		} else {
			v.setGrowthValue(cfg.getGrowthValue());
			v.setGrowthValueTitle(String.valueOf(cfg.getGrowthValue()));
		}

		if (cfg.getCoinsValue() < 0) {
			if (cfg.getlCoinsValue() >= 0 && cfg.getrCoinsValue() >= 0) {
				v.setlCoinsValue(cfg.getlCoinsValue());
				v.setrCoinsValue(cfg.getrCoinsValue());
				v.setCoinsValueTitle(v.getlCoinsValue() + "~" + v.getrCoinsValue());
			}
		} else {
			v.setCoinsValue(cfg.getCoinsValue());
			v.setCoinsValueTitle(String.valueOf(cfg.getCoinsValue()));
		}

		if (cfg.getrActiveStar() > cfg.getActiveStar()) {
			v.setShowStar(true);
			v.setMaxStar(cfg.getrActiveStar());
		} else {
			if (cfg.getActiveStar() != -1) {
				v.setShowStar(true);
				v.setMaxStar(cfg.getActiveStar());
			}
		}

		v.setHasNextTask(false);
		if (userTask.getType() == UserTaskType.ACHIEVEMENT) {
			if (userTask.getAchievementType() == UserTaskAchievementType.LEVEL && userTask.getCode() != 101020011) {
				v.setHasNextTask(true);
			} else if (userTask.getAchievementType() == UserTaskAchievementType.HOMEWORK_RANK
					&& userTask.getCode() != 101020014) {
				v.setHasNextTask(true);
			} else if (userTask.getAchievementType() == UserTaskAchievementType.EXERCISE
					&& userTask.getCode() != 101020018) {
				v.setHasNextTask(true);
			} else if (userTask.getAchievementType() == UserTaskAchievementType.SIGN_IN
					&& userTask.getCode() != 101020021) {
				v.setHasNextTask(true);
			}
		}

		/*
		 * if (cfg.getActiveStar() < 0) { if (cfg.getlActiveStar() >= 0 &&
		 * cfg.getrActiveStar() >= 0) { v.setlActiveStar(cfg.getlActiveStar());
		 * v.setrActiveStar(cfg.getrActiveStar());
		 * v.setActiveStarTitle(cfg.getlActiveStar() + "~" +
		 * cfg.getrActiveStar()); } } else {
		 * v.setActiveStar(cfg.getActiveStar());
		 * v.setActiveStarTitle(String.valueOf(cfg.getActiveStar())); }
		 */
		if (CollectionUtils.isNotEmpty(cfg.getItems())) {
			int index = 0;
			List<Map<String, Object>> items = new ArrayList<Map<String, Object>>(cfg.getItems().size());
			for (String item : cfg.getItems()) {
				Map<String, Object> m = new HashMap<String, Object>(5);
				m.put("content", item);
				m.put("coins", CollectionUtils.isEmpty(cfg.getItemCoins()) ? null : cfg.getItemCoins().get(index));
				m.put("growth", CollectionUtils.isEmpty(cfg.getItemGrowth()) ? null : cfg.getItemGrowth().get(index));
				m.put("star", CollectionUtils.isEmpty(cfg.getItemStar()) ? null : cfg.getItemStar().get(index));
				m.put("index", index + 1);

				items.add(m);

				index++;
			}

			v.setItems(items);
		}
		v.setUrl(cfg.getUrl());
		v.setIcon(FileUtil.getUrl(userTask.getIcon()));
		v.setAchievementType(userTask.getAchievementType());

		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VUserTask, UserTask, Integer, VUserTaskLog>() {
			@Override
			public boolean accept(UserTask userTask) {
				return userTask.isInitUserTaskLog();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(UserTask userTask, VUserTask vUserTask) {
				return userTask.getCode();
			}

			@Override
			public void setValue(UserTask userTask, VUserTask vUserTask, VUserTaskLog value) {
				vUserTask.setLog(value);
			}

			@Override
			public VUserTaskLog getValue(Integer key) {
				List<Integer> keys = Lists.newArrayList(key);
				Map<Integer, UserTaskLog> logMap = userTaskLogService.query(keys, Security.getUserId());
				UserTaskLog log = logMap.get(key);
				if (log == null) {
					log = new UserTaskLog();
					log.setStatus(UserTaskLogStatus.TASKING);
					log.setUserId(Security.getUserId());
					log.setId(-1L);
				} else {
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					long nowDay0 = Long.valueOf(format.format(new Date()));
					if (log.getTaskType() == UserTaskType.DAILY
							&& Long.valueOf(format.format(log.getCreateAt())) != nowDay0) {
						log = new UserTaskLog();
						log.setStatus(UserTaskLogStatus.TASKING);
						log.setUserId(Security.getUserId());
						log.setId(-1L);
					}
				}
				return userTaskLogConvert.to(log);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Integer, VUserTaskLog> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				long nowDay0 = Long.valueOf(format.format(new Date()));
				Map<Integer, UserTaskLog> logMap = userTaskLogService.query(keys, Security.getUserId());
				Map<Integer, VUserTaskLog> retMap = new HashMap<Integer, VUserTaskLog>(logMap.size());
				if (logMap.size() == 0) {
					for (Integer key : keys) {
						UserTaskLog l = new UserTaskLog();
						l.setStatus(UserTaskLogStatus.TASKING);
						l.setUserId(Security.getUserId());
						l.setId(-1L);

						retMap.put(key, userTaskLogConvert.to(l));
					}

					return retMap;
				} else {
					for (Map.Entry<Integer, UserTaskLog> e : logMap.entrySet()) {
						retMap.put(e.getKey(), userTaskLogConvert.to(e.getValue()));
					}

					for (Integer key : keys) {
						VUserTaskLog log = retMap.get(key);
						if (log == null) {
							UserTaskLog l = new UserTaskLog();
							l.setStatus(UserTaskLogStatus.TASKING);
							l.setUserId(Security.getUserId());
							l.setId(-1L);

							retMap.put(key, userTaskLogConvert.to(l));
						} else {
							if (log.getType() == UserTaskType.DAILY
									&& Long.valueOf(format.format(log.getCreateAt())) != nowDay0) {
								UserTaskLog l = new UserTaskLog();
								l.setStatus(UserTaskLogStatus.TASKING);
								l.setUserId(Security.getUserId());
								l.setId(-1L);

								retMap.put(key, userTaskLogConvert.to(l));
							}
						}

					}
					return retMap;
				}
			}
		});
	}
}
