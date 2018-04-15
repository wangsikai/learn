package com.lanking.uxb.zycon.task.api.impl.init;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStatus;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoal;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;
import com.lanking.uxb.zycon.homework.api.ZycStudentService;
import com.lanking.uxb.zycon.task.api.ZycDoQuestionGoalService;
import com.lanking.uxb.zycon.task.api.ZycUserTaskLogService;
import com.lanking.uxb.zycon.task.api.ZycUserTaskService;
import com.lanking.uxb.zycon.task.form.ZycUserTaskForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Service
public class ZycUserTaskSetGoalInitService {
	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private ZycUserTaskService zycUserTaskService;
	@Autowired
	private ZycUserTaskLogService zycUserTaskLogService;
	@Autowired
	private ZycStudentService zycStudentService;
	@Autowired
	private ZycDoQuestionGoalService doQuestionGoalService;
	@Autowired
	private CoinsLogService coinsLogService;

	private static final int FETCH_SIZE = 500;

	public void disabled() {
		ZycUserTaskForm taskForm = new ZycUserTaskForm();
		taskForm.setCode(getCode());
		taskForm.setUserTaskStatus(UserTaskStatus.PROCESS_DATA);
		zycUserTaskService.update(taskForm);

		CursorPage<Long, UserTaskLog> logPage = zycUserTaskLogService
				.fetch(CP.cursor(Long.MAX_VALUE, FETCH_SIZE), getCode());

		while (logPage.isNotEmpty()) {
			List<Long> ids = new ArrayList<Long>(logPage.getItems().size());

			for (UserTaskLog l : logPage.getItems()) {
				ids.add(l.getId());
			}

			zycUserTaskLogService.disabled(ids);

			logPage = zycUserTaskLogService.fetch(CP.cursor(logPage.getLast().getId(), FETCH_SIZE), getCode());
		}

		taskForm.setUserTaskStatus(UserTaskStatus.DISABLED);
		zycUserTaskService.update(taskForm);

	}

	public void init() {
		ZycUserTaskForm taskForm = new ZycUserTaskForm();
		taskForm.setCode(getCode());
		taskForm.setUserTaskStatus(UserTaskStatus.PROCESS_DATA);
		zycUserTaskService.update(taskForm);
		CursorPageable<Long> pageable = CP.cursor(Long.MAX_VALUE, FETCH_SIZE);
		CursorPage<Long, Student> page = zycStudentService.query(pageable);

		UserTask userTask = userTaskService.get(getCode());
		UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();

		while (page.isNotEmpty()) {
			List<Student> students = page.getItems();

			List<Long> userIds = new ArrayList<Long>(students.size());
			for (Student s : students) {
				userIds.add(s.getId());
			}

			Map<Long, UserTaskLog> logMap = zycUserTaskLogService.mgetByCodeAndUsers(userIds, getCode());

			for (long userId : userIds) {
				UserTaskLog log = logMap.get(userId);

				DoQuestionGoal goal = doQuestionGoalService.findByUserId(userId);
				if (log == null) {
					if (goal != null) {
						int coins = cfg.getCoinsValue();
						int growth = cfg.getGrowthValue();

						UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
						form.setUserId(userId);
						form.setTaskCode(userTask.getCode());
						form.setType(userTask.getType());
						form.setGrowth(growth);
						form.setCoins(coins);
						form.setStatus(UserTaskLogStatus.COMPLETE);

						JSONObject contentJsonObj = new JSONObject();
						contentJsonObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
						contentJsonObj.put("completeAt", System.currentTimeMillis());
						contentJsonObj.put("items", Collections.EMPTY_LIST);
						form.setContent(contentJsonObj.toJSONString());
						form.setDate(new Date());

						userTaskLogService.update(form);
					}
				} else {
					if (log.getStatus() == UserTaskLogStatus.DISABLED) {
						UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
						form.setId(log.getId());
						CoinsLog coinsLog = coinsLogService.find(getCode(), userId, null);
						if (coinsLog == null) {
							if (goal != null) {
								form.setStatus(UserTaskLogStatus.COMPLETE);
							} else {
								form.setStatus(UserTaskLogStatus.TASKING);
							}
						} else {
							form.setStatus(UserTaskLogStatus.RECEIVE);
						}

						userTaskLogService.update(form);
					}
				}
			}

			page = zycStudentService.query(CP.cursor(page.getLast().getId(), FETCH_SIZE));
		}

		zycUserTaskService.completeInitData(getCode());
	}

	public int getCode() {
		return 101000002;
	}
}
