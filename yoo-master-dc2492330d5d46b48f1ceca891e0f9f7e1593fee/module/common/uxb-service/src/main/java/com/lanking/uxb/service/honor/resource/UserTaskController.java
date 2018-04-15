package com.lanking.uxb.service.honor.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.UserLevels;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskAchievementType;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStarLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskType;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskUserScope;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.api.UserLevelsService;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.api.UserTaskStarLogService;
import com.lanking.uxb.service.honor.convert.UserTaskConvert;
import com.lanking.uxb.service.honor.ex.HonorException;
import com.lanking.uxb.service.honor.form.UserTaskQueryForm;
import com.lanking.uxb.service.honor.form.UserTaskRuleType;
import com.lanking.uxb.service.honor.value.VUserHonor;
import com.lanking.uxb.service.honor.value.VUserReward;
import com.lanking.uxb.service.honor.value.VUserTask;
import com.lanking.uxb.service.honor.value.VUserTaskLog;
import com.lanking.uxb.service.honor.value.VUserTaskStarLog;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;

/**
 * @author xinyu.zhou
 * @since 4.0.0
 */
@RestController
@RequestMapping(value = "userTask")
public class UserTaskController {

	private Logger logger = LoggerFactory.getLogger(UserTaskController.class);

	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private UserTaskConvert userTaskConvert;
	@Autowired
	private UserService userService;
	@Autowired
	private UserTaskStarLogService starLogService;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private UserLevelsService userLevelsService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private ZyStudentHomeworkService zyStuHkService;
	@Autowired
	private MqSender mqSender;

	/**
	 * 查找所有用户任务
	 *
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER", "STUDENT" })
	@RequestMapping(value = "spec", method = { RequestMethod.GET, RequestMethod.POST })
	public Value spec(UserTaskRuleType ruleType) {
		UserTaskQueryForm form = new UserTaskQueryForm();
		UserType userType = Security.getUserType();
		form.setUserType(userType);
		form.setStatus(UserTaskStatus.OPEN);
		User user = userService.get(Security.getUserId());
		form.setScope(user.getUserChannelCode() > 10000 ? UserTaskUserScope.CHANNEL_USER
				: UserTaskUserScope.SELF_REGISTRATION);

		List<UserTask> taskList = userTaskService.find(form);
		if (CollectionUtils.isEmpty(taskList)) {
			return new Value(Collections.EMPTY_MAP);
		}
		if (ruleType != null) {
			List<UserTask> ruleTaskList = Lists.newArrayList();
			for (UserTask ut : taskList) {
				if (ruleType == UserTaskRuleType.COINS_RULE && ut.getUserTaskRuleCfg().getCoinsValue() != 0) {
					ruleTaskList.add(ut);
				}
				if (ruleType == UserTaskRuleType.GROWTH_RULE && ut.getUserTaskRuleCfg().getGrowthValue() != 0) {
					ruleTaskList.add(ut);
				}
			}
			taskList = ruleTaskList;
		}

		List<VUserTask> vtasks = userTaskConvert.to(taskList);

		Map<UserTaskType, List<VUserTask>> taskMap = new HashMap<UserTaskType, List<VUserTask>>(3);
		for (VUserTask t : vtasks) {
			List<VUserTask> tasks = taskMap.get(t.getType());
			if (CollectionUtils.isEmpty(tasks)) {
				tasks = Lists.newArrayList();
			}

			tasks.add(t);
			taskMap.put(t.getType(), tasks);
		}

		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>(3);

		for (int i = 0; i <= 2; i++) {
			Map<String, Object> m = new HashMap<String, Object>(3);
			UserTaskType type = UserTaskType.findByValue(i);
			if (type == null) {
				break;
			}
			m.put("type", type);
			m.put("typeName", type.getName());
			m.put("tasks", taskMap.get(type));

			retList.add(m);
		}

		return new Value(retList);
	}

	/**
	 * 用户任务首页相关数据接口
	 *
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "index", method = { RequestMethod.GET, RequestMethod.POST })
	public Value index() {
		// 每日任务、成就任务、新手任务 任务列表
		UserTaskQueryForm form = new UserTaskQueryForm();
		UserType userType = Security.getUserType();
		form.setUserType(userType);
		form.setStatus(UserTaskStatus.OPEN);
		User user = userService.get(Security.getUserId());
		form.setScope(user.getUserChannelCode() > 10000 ? UserTaskUserScope.CHANNEL_USER
				: UserTaskUserScope.SELF_REGISTRATION);

		// 取用户level
		VUserHonor userHonor = getHonor();

		List<UserTask> taskList = userTaskService.find(form);
		Map<String, Object> retMap = new HashMap<String, Object>(5);
		List<VUserTask> vtasks = userTaskConvert.to(taskList, true);
		Integer todoHkCount = zyStuHkService.countToDoHk(Security.getUserId());
		Map<UserTaskType, List<VUserTask>> taskMap = new HashMap<UserTaskType, List<VUserTask>>(3);
		boolean hasUnCompleteNewUserTask = false;
		for (VUserTask t : vtasks) {
			List<VUserTask> tasks = taskMap.get(t.getType());
			if (CollectionUtils.isEmpty(tasks)) {
				tasks = Lists.newArrayList();
			}
			if (t.getCode() == 101010002 && todoHkCount == 0 && t.getLog() == null) {
				continue;
			}
			// 如果是签到任务显示完成
			if (t.getCode() == 101010001 && t.getLog().getStatus() == UserTaskLogStatus.TASKING) {
				Date date = new Date();
				String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
				Date yesterday = null;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
					yesterday = DateUtils.addDays(date, -1);
				} catch (ParseException e) {
				}
				UserTaskLog yesterdayLog = userTaskLogService.findByCodeAndUser(t.getCode(), Security.getUserId(),
						yesterday);
				// 补填星星(签到)
				List<Map<String, Object>> items = Lists.newArrayList();
				Map<String, Object> map = null;
				if (yesterdayLog != null) {
					map = new HashMap<String, Object>();
					map.put("index", 1);
					map.put("completeAt", System.currentTimeMillis());
					items.add(map);
					map = new HashMap<String, Object>();
					map.put("index", 2);
					map.put("completeAt", System.currentTimeMillis());
					items.add(map);
					t.getLog().getDetail().put("items", items);
					t.getLog().setStar(2);
				} else {
					map = new HashMap<String, Object>();
					map.put("index", 1);
					map.put("completeAt", System.currentTimeMillis());
					items.add(map);
					t.getLog().getDetail().put("items", items);
					t.getLog().setStar(1);
				}
				t.getLog().setStatus(UserTaskLogStatus.COMPLETE);
			}

			if (t.getType() == UserTaskType.NEW_USER && t.getLog().getStatus() == UserTaskLogStatus.TASKING) {
				hasUnCompleteNewUserTask = true;
			}
			tasks.add(t);
			taskMap.put(t.getType(), tasks);
		}

		if (!hasUnCompleteNewUserTask) {
			// 没有未完成的新手任务
			Date date = userTaskLogService.getLatestCompleteDate(UserTaskType.NEW_USER, Security.getUserId());
			if (date == null) {
				retMap.put("showNewUserReward", false);
			} else {
				if (System.currentTimeMillis() - date.getTime() > TimeUnit.HOURS.toMillis(24)) {
					retMap.put("showNewUserReward", false);
				} else {
					retMap.put("showNewUserReward", true);
				}
			}
		} else {
			retMap.put("showNewUserReward", true);
		}
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>(3);

		for (int i = 0; i <= 2; i++) {
			Map<String, Object> m = new HashMap<String, Object>(3);
			UserTaskType type = UserTaskType.findByValue(i);
			if (type == null) {
				break;
			}
			m.put("type", type);
			m.put("typeName", type.getName());

			List<VUserTask> vtaskList = taskMap.get(type);

			// 新手log任务为空处理
			if (type == UserTaskType.NEW_USER && vtaskList != null) {
				for (int j = 0; j < vtaskList.size(); j++) {
					VUserTask task = vtaskList.get(j);
					this.handleTaskLog(task, userHonor);
				}
			}

			// 处理成就任务
			if (type == UserTaskType.ACHIEVEMENT && vtaskList != null) {
				vtaskList = this.handleAchievementUserTasks(vtaskList, userHonor);
			}

			m.put("tasks", vtaskList == null ? Collections.EMPTY_LIST : vtaskList);
			retList.add(m);
		}
		retMap.put("userTasks", retList);
		Date date = new Date();
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
		} catch (ParseException e) {
		}

		// 用户宝箱数据情况
		UserTaskStarLog starLog = starLogService.query(date, Security.getUserId());
		VUserTaskStarLog vUserTaskStarLog = new VUserTaskStarLog();
		Map<Integer, Boolean> logsMap = null;
		if (starLog == null) {
			vUserTaskStarLog.setId(-1);
			vUserTaskStarLog.setUserId(Security.getUserId());
			vUserTaskStarLog.setStar(0);
		} else {
			vUserTaskStarLog.setStar(starLog.getStar());
			String takeContent = starLog.getContent();
			if (StringUtils.isNotBlank(takeContent)) {
				JSONObject jsonObject = JSONObject.parseObject(takeContent);
				JSONArray takeLogs = jsonObject.getJSONArray("takeLogs");
				if (takeLogs != null && takeLogs.size() > 0) {

					logsMap = new HashMap<Integer, Boolean>(takeLogs.size());
					for (Object o : takeLogs) {
						JSONObject obj = JSONObject.parseObject(o.toString());
						logsMap.put(obj.getInteger("star"), true);
					}
				}
			}
		}
		String treasureRuleValue = parameterService.get(Product.YOOMATH, "userTask.treasure.rule").getValue();
		if (StringUtils.isNotBlank(treasureRuleValue)) {
			JSONObject treasureRuleJson = JSONObject.parseObject(treasureRuleValue);
			JSONArray levelArr = treasureRuleJson.getJSONArray("starLevels");

			List<Map<String, Object>> levelMap = new ArrayList<Map<String, Object>>(levelArr.size());
			for (Object o : levelArr) {
				JSONObject oj = JSONObject.parseObject(o.toString());
				Map<String, Object> lm = new HashMap<String, Object>(8);
				int starStandard = oj.getInteger("star");
				lm.put("coins", oj.get("coins"));
				lm.put("star", oj.get("star"));
				lm.put("growth", oj.get("growth"));
				lm.put("vipExtraCoins", oj.get("vipExtraCoins"));
				lm.put("vipExtraGrowth", oj.get("vipExtraGrowth"));
				lm.put("canOpen", vUserTaskStarLog.getStar() >= starStandard);
				lm.put("opened", logsMap != null && logsMap.size() != 0 && logsMap.get(starStandard) != null);

				levelMap.add(lm);
			}

			vUserTaskStarLog.setStarLevels(levelMap);
		}

		retMap.put("starTreasure", vUserTaskStarLog);
		retMap.put("honor", userHonor);
		retMap.put("user", userConvert.get(Security.getUserId(), new UserConvertOption(true)));

		return new Value(retMap);
	}

	private VUserHonor getHonor() {
		UserHonor honor = userHonorService.getUserHonor(Security.getUserId());
		VUserHonor vhonor = new VUserHonor();
		// 如果为空,说明用户还没有做任何提高荣誉的操作
		if (honor == null) {
			vhonor.setCoins(0);
			vhonor.setGrowth(0);
			vhonor.setLevel(1);
			vhonor.setNextLevel(2);
			UserLevels ul = userLevelsService.getUserLevel(2, Product.YOOMATH);
			vhonor.setUpNeedGrowth(ul.getMinGrowthValue());
			vhonor.setLevels(userLevelsService.getUserLevel(0, UserLevelsService.MIDDLELEVEL, Product.YOOMATH));
		} else {
			vhonor.setCoins(honor.getCoins());
			vhonor.setGrowth(honor.getGrowth());
			vhonor.setLevel(honor.getLevel());
			if (honor.getLevel() != UserLevelsService.MAXLEVEL) {
				vhonor.setNextLevel(honor.getLevel() + 1);
				UserLevels ul = userLevelsService.getUserLevel(honor.getLevel() + 1, Product.YOOMATH);
				vhonor.setUpNeedGrowth(ul.getMinGrowthValue() - honor.getGrowth());
			}
		}

		return vhonor;
	}

	/**
	 * 成就任务需要过滤任务列表<br>
	 * 同一类型的任务只显示一个，如果已领取并且有后续任务则不显示.
	 * 
	 * @param vtaskList
	 *            任务列表
	 * @param userHonor
	 *            用户荣誉数据
	 */
	private List<VUserTask> handleAchievementUserTasks(List<VUserTask> vtaskList, VUserHonor userHonor) {
		Set<Integer> hasTypes = new HashSet<Integer>(); // 成就任务类型
		List<VUserTask> newList = new ArrayList<VUserTask>();
		boolean levelFlag = false;
		boolean minlevelFlag = false;
		for (int j = 0; j < vtaskList.size(); j++) {
			VUserTask task = vtaskList.get(j);

			// 处理成就任务log为空的情况
			this.handleTaskLog(task, userHonor);
			// 等级任务
			if (task.getAchievementType() == UserTaskAchievementType.LEVEL) {
				int taskCode = getTaskCodeByLevel(userHonor.getLevel());
				// 1级用户展示lv2任务
				if (taskCode == 0 && task.getCode() == 101020001) {
					newList.add(task);
					hasTypes.add(task.getAchievementType().getValue());
					continue;
				}
				// 展示已完成等级最低的任务
				if (minlevelFlag) {
					continue;
				}
				if (task.getCode() <= taskCode && task.getLog().getStatus() == UserTaskLogStatus.COMPLETE) {
					// 判断当前等级下有没有已完成的任务
					newList.add(task);
					hasTypes.add(task.getAchievementType().getValue());
					minlevelFlag = true;
				} else if (task.getCode() == taskCode && task.getLog().getStatus() != UserTaskLogStatus.COMPLETE
						&& !minlevelFlag) {
					levelFlag = true;
				}
				if (levelFlag) {
					if (userHonor.getLevel() < 12) {
						int nexttaskCode = getTaskCodeByLevel(userHonor.getLevel() + 1);
						if (task.getCode() == nexttaskCode) {
							newList.add(task);
							hasTypes.add(task.getAchievementType().getValue());
						}
					} else {
						// 达到12级展示LV12任务
						if (taskCode == task.getCode()) {
							newList.add(task);
							hasTypes.add(task.getAchievementType().getValue());
						}
					}

				}

			} else {
				if ((task.getLog().getStatus() == UserTaskLogStatus.TASKING
						|| task.getLog().getStatus() == UserTaskLogStatus.COMPLETE)
						&& !hasTypes.contains(task.getAchievementType().getValue())) {
					// 未领取的任务
					newList.add(task);
					hasTypes.add(task.getAchievementType().getValue());
				} else if (task.getLog().getStatus() == UserTaskLogStatus.RECEIVE
						&& !hasTypes.contains(task.getAchievementType().getValue())) {
					// 已领取的任务，并且后续已经没有该类型的任务了
					if (j + 1 >= vtaskList.size()
							|| vtaskList.get(j + 1).getAchievementType() != task.getAchievementType()) {
						newList.add(task);
						hasTypes.add(task.getAchievementType().getValue());
					}
				}
			}
		}

		return newList;
	}

	/**
	 * 处理index中返回任务log为空的情况.
	 * 
	 * @param userTask
	 */
	private void handleTaskLog(VUserTask userTask, VUserHonor vhonor) {
		if (userTask.getType() == UserTaskType.NEW_USER) {
			int allCount = userTask.getItems() == null ? 0 : userTask.getItems().size();
			if (userTask.getLog() != null && userTask.getLog().getId() != -1) {
				Map<String, Object> map = userTask.getLog().getDetail();
				int doCount = 0;
				if (map != null) {
					List<Object> items = (List<Object>) map.get("items");
					doCount = items.size();
				}
				if (doCount == 0 && map.get("completeAt") != null) {
					doCount = allCount;
				}
				userTask.getLog().setCompleteTitle(doCount + "/" + allCount);
				return;
			}
			if (userTask.getLog() != null && userTask.getLog().getCompleteTitle() == null) {
				userTask.getLog().setCompleteTitle("0/" + allCount);
				return;
			}
		}

		if (userTask.getType() == UserTaskType.ACHIEVEMENT) {
			if (userTask.getLog() != null && userTask.getLog().getId() != -1) {
				return;
			}
			if (userTask.getLog() == null) {
				VUserTaskLog log = userTask.getLog();
				log.setStatus(UserTaskLogStatus.TASKING);
				log.setUserId(Security.getUserId());
				log.setId(-1L);
				userTask.setLog(log);
			}
			if (userTask.getAchievementType() == UserTaskAchievementType.EXERCISE) {
				// 自主练习任务
				userTask.getLog().setCompleteTitle("0/1000");
			} else if (userTask.getAchievementType() == UserTaskAchievementType.HOMEWORK_ADVANCE) {
				// 作业进步任务
				userTask.getLog().setCompleteTitle("0/1");
			} else if (userTask.getAchievementType() == UserTaskAchievementType.HOMEWORK_RANK) {
				// 作业排名任务
				userTask.getLog().setCompleteTitle("0/1");
			} else if (userTask.getAchievementType() == UserTaskAchievementType.LEVEL) {
				int growth = vhonor.getGrowth();
				int allGrowth = growth + vhonor.getUpNeedGrowth();
				// 等级任务
				userTask.getLog().setCompleteTitle(growth + "/" + allGrowth);
			} else if (userTask.getAchievementType() == UserTaskAchievementType.SIGN_IN) {
				// 累计签到任务
				userTask.getLog().setCompleteTitle("0/30");
			}
		}
	}

	private int getTaskCodeByLevel(int level) {
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
	 * 领取奖励
	 * 
	 * @param id
	 *            用户任务log.id
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "getReward", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getReward(long id, int taskCode) {
		if (taskCode == 101010001) {
			// 增加判断任务是否存在
			UserTask uTask = userTaskService.get(taskCode);
			if (uTask == null) {
				return new Value(new HonorException(HonorException.HONOR_USERTASK_NOT_EXISTS));
			} else {
				if (UserTaskStatus.OPEN != uTask.getStatus()) {
					return new Value(new HonorException(HonorException.HONOR_USERTASK_NOT_EXISTS));
				}
			}

			Date date = new Date();
			String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
			} catch (ParseException e) {
			}
			UserTaskLog log = userTaskLogService.findByCodeAndUser(taskCode, Security.getUserId(), date);
			if (log != null) {
				return new Value(new HonorException(HonorException.HONOR_USERTASK_GETED_REWARD));
			}
			// 签到
			JSONObject messageObj = new JSONObject();
			messageObj.put("taskCode", 101010001);
			messageObj.put("userId", Security.getUserId());
			messageObj.put("isClient", Security.isClient());
			mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG_101010001,
					MQ.builder().data(messageObj).build());
			int total = 0;
			long sleep = 500;
			sleep(sleep);
			log = userTaskLogService.findByCodeAndUser(taskCode, Security.getUserId(), date);
			while (log == null && total < 3000) {
				sleep(sleep);
				total += sleep;
				logger.debug("sleep:{}", sleep);
				log = userTaskLogService.findByCodeAndUser(taskCode, Security.getUserId(), date);
			}
			UserHonor userHonor = userHonorService.getUserHonor(Security.getUserId());
			// 防止log为空的情况
			if (log == null) {
				logger.error("taskCode:101010001,userTaskLog数据获取失败.");
				return new Value(new HonorException(HonorException.HONOR_USERTASK_NOT_EXISTS));
			} else {
				// 构造返回值
				VUserReward v = new VUserReward();
				v.setCoinsValue(log.getCoins());
				v.setGrowthValue(log.getGrowth());
				v.setStarValue(log.getStar());
				v.setUpGrade(userHonor.isUpgrade());
				// 设置下一等级信息
				VUserHonor vhonor = getHonor();
				v.setNextLevel(vhonor.getNextLevel());
				v.setUpNeedGrowth(vhonor.getUpNeedGrowth());
				v.setLevel(vhonor.getLevel());
				return new Value(v);
			}
		}

		Map<String, Object> logMap = null;
		try {
			logMap = userTaskLogService.earn(id);
		} catch (HonorException e) {
			return new Value(e);
		}
		if (logMap.isEmpty()) {
			return new Value(new IllegalArgException());
		}
		GrowthLog growthLog = (GrowthLog) logMap.get("growthLog");
		CoinsLog coinsLog = (CoinsLog) logMap.get("coinsLog");
		UserHonor userHonor = (UserHonor) logMap.get("honor");

		Integer star = (Integer) logMap.get("star");
		Integer extraStar = (Integer) logMap.get("extraStar");

		VUserReward v = new VUserReward();
		v.setCoinsValue(coinsLog == null ? 0 : coinsLog.getCoinsValue());
		v.setGrowthValue(growthLog == null ? 0 : growthLog.getGrowthValue());
		if (star != null && star > 0) {
			v.setStarValue(star);
		}
		if (extraStar != null && extraStar > 0) {
			v.setMemberExtraCoinsValue(extraStar);
		}
		v.setUpGrade(userHonor.isUpgrade());
		// 设置下一等级信息
		VUserHonor vhonor = getHonor();
		v.setNextLevel(vhonor.getNextLevel());
		v.setUpNeedGrowth(vhonor.getUpNeedGrowth());
		v.setLevel(vhonor.getLevel());

		return new Value(v);
	}

	/**
	 * @param i
	 */
	private void sleep(long i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 领取星级宝箱奖励
	 *
	 * @param star
	 *            星级
	 * @return {@link Value}
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "getTreasure", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getTreasure(Integer star) {
		if (star == null || star < 1) {
			return new Value(new IllegalArgException());
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(format.format(date));
		} catch (Exception e) {
		}
		UserTaskStarLog log = starLogService.query(date, Security.getUserId());
		if (log == null) {
			return new Value(new HonorException(HonorException.HONOR_USERTASK_NOT_EXISTS));
		}
		MemberType memberType = SecurityContext.getMemberType();
		int index = 0, rewardCoins = 0, rewardGrowth = 0, extraCoins = 0, extraGrowth = 0;
		String treasureRuleValue = parameterService.get(Product.YOOMATH, "userTask.treasure.rule").getValue();
		if (StringUtils.isNotBlank(treasureRuleValue)) {
			JSONObject treasureRuleJson = JSONObject.parseObject(treasureRuleValue);
			JSONArray levelArr = treasureRuleJson.getJSONArray("starLevels");

			List<Map<String, Object>> levelMap = new ArrayList<Map<String, Object>>(levelArr.size());
			int i = 1;
			for (Object o : levelArr) {
				JSONObject oj = JSONObject.parseObject(o.toString());

				if (oj.getInteger("star") == star) {
					index = i;

					rewardCoins = oj.getInteger("coins") == null ? 0 : oj.getInteger("coins");
					rewardGrowth = oj.getInteger("growth") == null ? 0 : oj.getInteger("growth");
					extraCoins = oj.getInteger("vipExtraCoins") == null ? 0 : oj.getInteger("vipExtraCoins");
					extraGrowth = oj.getInteger("vipExtraGrowth") == null ? 0 : oj.getInteger("vipExtraGrowth");
					break;
				}

				i++;
			}

		}

		if (index == 0) {
			return new Value(new HonorException(HonorException.HONOR_USERTASK_NOT_EXISTS));
		}
		if (log.getStar() == 0 || log.getStar() < star) {
			return new Value(new NoPermissionException());
		}
		int insertIndex = 0;
		List<Map<String, Object>> mlist = new ArrayList<Map<String, Object>>(3);
		if (StringUtils.isNotBlank(log.getContent())) {
			JSONObject jsonObject = JSONObject.parseObject(log.getContent());
			JSONArray takeLogArr = jsonObject.getJSONArray("takeLogs");

			for (Object o : takeLogArr) {
				JSONObject logObj = JSONObject.parseObject(o.toString());

				if (logObj.getInteger("star") == star) {
					return new Value(new HonorException(HonorException.HONOR_USERTASK_GETED_REWARD));
				}

				if (star > logObj.getInteger("star")) {
					insertIndex++;
				}

				Map<String, Object> m = new HashMap<String, Object>(5);
				m.put("star", logObj.get("star"));
				m.put("coins", logObj.get("coins"));
				m.put("growth", logObj.get("growth"));
				m.put("vipExtraCoins", logObj.get("vipExtraCoins"));
				m.put("vipExtraGrowth", logObj.get("vipExtraGrowth"));

				mlist.add(m);
			}
		}

		VUserReward v = new VUserReward();
		v.setCoinsValue(rewardCoins);
		v.setGrowthValue(rewardGrowth);
		v.setStarValue(star);
		v.setMemberExtraCoinsValue(extraCoins);
		v.setMemberExtraGrowthValue(extraGrowth);

		JSONObject jsonObject = new JSONObject();

		Map<String, Object> m = new HashMap<String, Object>(6);
		m.put("star", star);
		m.put("coins", rewardCoins);
		m.put("growth", rewardGrowth);
		m.put("vipExtraCoins", extraCoins);
		m.put("vipExtraGrowth", extraGrowth);
		mlist.add(insertIndex, m);
		jsonObject.put("takeLogs", mlist);

		int coinsValue = 0;
		int growthValue = 0;
		if (memberType != null && (memberType == MemberType.VIP || memberType == MemberType.SCHOOL_VIP)) {
			coinsValue = rewardCoins + extraCoins;
			growthValue = rewardGrowth + extraGrowth;
		} else {
			coinsValue = rewardCoins;
			growthValue = rewardGrowth;
		}
		// 注意现在按照 柴林森的要求 -> 完成宝箱不清星星数
		// 若以后这个B要求改成减少星星数，则此方法调用时写成
		// starLogService.earn(Security.getUserId(), -star,
		// jsonObject.toJSONString(), date, coinsValue, growthValue);
		starLogService.earn(Security.getUserId(), 0, jsonObject.toJSONString(), date, coinsValue, growthValue);

		// 设置下一等级信息
		VUserHonor vhonor = getHonor();
		v.setNextLevel(vhonor.getNextLevel());
		v.setUpNeedGrowth(vhonor.getUpNeedGrowth());
		v.setLevel(vhonor.getLevel());

		Map<String, Object> retMap = new HashMap<String, Object>(2);
		retMap.put("userReward", v);
		retMap.put("memberType", memberType);

		return new Value(retMap);
	}

	/**
	 * 完成悠数学引导
	 *
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "completeUserGuide", method = { RequestMethod.GET, RequestMethod.POST })
	public Value completeUserGuide() {
		CoinsLog coinsLog = coinsLogService.find(101000005, Security.getUserId(), null);
		if (coinsLog != null) {
			return new Value(new HonorException(HonorException.HONOR_USERTASK_GETED_REWARD));
		}

		UserTask userTask = userTaskService.get(101000005);
		if (userTask == null) {
			return new Value(new HonorException(HonorException.HONOR_USERTASK_NOT_EXISTS));
		}
		userTaskLogService.receiveReward(userTask, Security.getUserId());

		VUserReward v = new VUserReward();
		v.setCoinsValue(userTask.getUserTaskRuleCfg().getCoinsValue());
		return new Value(v);
	}

	/**
	 * 判断是否领取了悠数学使用引导奖励
	 *
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "hasGetUserGuideReward", method = { RequestMethod.GET, RequestMethod.POST })
	public Value hasGetUserGuideReward() {
		CoinsLog coinsLog = coinsLogService.find(101000005, Security.getUserId(), null);
		Map<String, Object> retMap = new HashMap<String, Object>(1);
		retMap.put("hasGet", coinsLog != null);
		return new Value(retMap);
	}

	/**
	 * 单独获取当前用户的成就任务列表.
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "findAchievementUserTasks", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findAchievementUserTasks() {

		UserTaskQueryForm form = new UserTaskQueryForm();
		form.setType(UserTaskType.ACHIEVEMENT); // 成就任务
		UserType userType = Security.getUserType();
		form.setUserType(userType);
		form.setStatus(UserTaskStatus.OPEN);
		User user = userService.get(Security.getUserId());
		form.setScope(user.getUserChannelCode() > 10000 ? UserTaskUserScope.CHANNEL_USER
				: UserTaskUserScope.SELF_REGISTRATION);

		List<UserTask> taskList = userTaskService.find(form);
		if (CollectionUtils.isEmpty(taskList)) {
			return new Value(Collections.EMPTY_MAP);
		}

		// 取用户level
		VUserHonor userHonor = getHonor();
		List<VUserTask> vtaskList = userTaskConvert.to(taskList, true);

		// 处理成就任务
		vtaskList = this.handleAchievementUserTasks(vtaskList, userHonor);

		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("honor", userHonor);
		map.put("tasks", vtaskList);

		return new Value(map);
	}
}
