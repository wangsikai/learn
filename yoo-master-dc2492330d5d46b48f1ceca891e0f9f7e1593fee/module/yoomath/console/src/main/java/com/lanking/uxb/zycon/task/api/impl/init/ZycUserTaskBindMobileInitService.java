package com.lanking.uxb.zycon.task.api.impl.init;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStatus;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.zycon.homework.api.ZycStudentService;
import com.lanking.uxb.zycon.task.api.ZycUserTaskLogService;
import com.lanking.uxb.zycon.task.api.ZycUserTaskService;
import com.lanking.uxb.zycon.task.form.ZycUserTaskForm;

/**
 * @author peng.zhao
 * @since 1.4.7
 */
@Service
public class ZycUserTaskBindMobileInitService {

	@Autowired
	private ZycUserTaskService zycUserTaskService;
	@Autowired
	private ZycStudentService studentService;
	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private ZycUserTaskLogService zycUserTaskLogService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private UserTaskLogService userTaskLogService;

	private static final int FETCH_SIZE = 500;

	public void init() {
		ZycUserTaskForm taskForm = new ZycUserTaskForm();
		taskForm.setCode(getCode());
		taskForm.setUserTaskStatus(UserTaskStatus.PROCESS_DATA);
		zycUserTaskService.update(taskForm);
		CursorPageable<Long> pageable = CP.cursor(Long.MAX_VALUE, FETCH_SIZE);
		CursorPage<Long, Student> page = studentService.query(pageable);
		UserTask userTask = userTaskService.get(getCode());
		UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();
		Date now = new Date();

		while (page.isNotEmpty()) {
			List<Long> userIds = page.getItems().stream().map(p -> p.getId()).collect(Collectors.toList());
			Map<Long, UserTaskLog> logMap = zycUserTaskLogService.mgetByCodeAndUsers(userIds, getCode());

			for (Long userId : userIds) {
				UserTaskLog log = logMap.get(userId);
				// 用户是否绑定过手机号
				Account account = accountService.getAccountByUserId(userId);
				// 以前没有任务数据
				if (log == null) {
					UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
					// 已经绑定过手机
					if (account != null && Status.ENABLED == account.getMobileStatus()) {
						// roleCode:127
						List<CoinsLog> coinsLogs = coinsLogService.findLogs(127, userId, null);

						int coins = cfg.getCoinsValue();
						int growth = cfg.getGrowthValue();

						form.setUserId(userId);
						form.setCoins(coins);
						form.setGrowth(growth);
						form.setTaskCode(userTask.getCode());
						JSONObject contentJsonObj = new JSONObject();
						contentJsonObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
						contentJsonObj.put("completeAt", System.currentTimeMillis());
						contentJsonObj.put("items", Collections.EMPTY_LIST);

						form.setContent(contentJsonObj.toJSONString());
						form.setDate(now);
						form.setType(userTask.getType());

						if (CollectionUtils.isNotEmpty(coinsLogs)) {
							form.setStatus(UserTaskLogStatus.RECEIVE);
						} else {
							form.setStatus(UserTaskLogStatus.COMPLETE);
						}

						userTaskLogService.update(form);
					}
				} else {
					// 存在任务数据
					if (log.getStatus() == UserTaskLogStatus.DISABLED) {
						UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
						form.setId(log.getId());
						// 分为以前已经领取过奖励、还有一种未领取奖励
						List<CoinsLog> coinsLogs = coinsLogService.findLogs(getCode(), userId, null);
						if (CollectionUtils.isEmpty(coinsLogs)) {
							// 已经绑定过,任务设置已完成
							if (account != null && Status.ENABLED == account.getMobileStatus()) {
								form.setStatus(UserTaskLogStatus.COMPLETE);
							} else {
								// 未绑定
								form.setStatus(UserTaskLogStatus.TASKING);
							}
						} else {
							form.setStatus(UserTaskLogStatus.RECEIVE);
						}

						userTaskLogService.update(form);
					}
				}
			}

			page = studentService.query(CP.cursor(page.getLast().getId(), FETCH_SIZE));
		}

		// 开启任务
		zycUserTaskService.completeInitData(getCode());
	}

	public void disabled() {
		ZycUserTaskForm taskForm = new ZycUserTaskForm();
		taskForm.setCode(getCode());
		taskForm.setUserTaskStatus(UserTaskStatus.PROCESS_DATA);
		zycUserTaskService.update(taskForm);

		CursorPage<Long, UserTaskLog> logPage = zycUserTaskLogService.fetch(CP.cursor(Long.MAX_VALUE, FETCH_SIZE),
				getCode());
		while (logPage.isNotEmpty()) {
			List<Long> ids = logPage.getItems().stream().map(p -> p.getId()).collect(Collectors.toList());
			zycUserTaskLogService.disabled(ids);

			logPage = zycUserTaskLogService.fetch(CP.cursor(logPage.getLast().getId(), FETCH_SIZE), getCode());
		}

		taskForm.setUserTaskStatus(UserTaskStatus.DISABLED);
		zycUserTaskService.update(taskForm);
	}

	public int getCode() {
		return 101000006;
	}
}
