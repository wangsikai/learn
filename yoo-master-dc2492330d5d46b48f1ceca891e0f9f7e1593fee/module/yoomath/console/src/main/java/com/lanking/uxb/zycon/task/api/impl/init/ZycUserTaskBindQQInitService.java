package com.lanking.uxb.zycon.task.api.impl.init;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStatus;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.zycon.homework.api.ZycStudentService;
import com.lanking.uxb.zycon.task.api.ZycUserTaskLogService;
import com.lanking.uxb.zycon.task.api.ZycUserTaskService;
import com.lanking.uxb.zycon.task.form.ZycUserTaskForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class ZycUserTaskBindQQInitService {
	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private ZycUserTaskService zycUserTaskService;
	@Autowired
	private ZycUserTaskLogService zycUserTaskLogService;
	@Autowired
	private ZycStudentService studentService;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private CoinsLogService coinsLogService;
	private static final int FETCH_SIZE = 500;
	private static final Logger logger = LoggerFactory.getLogger(ZycUserTaskBindQQInitService.class);

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
		CursorPage<Long, Student> page = studentService.query(pageable);
		UserTask userTask = userTaskService.get(getCode());
		UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();
		Date now = new Date();
		logger.info("begin init qq user_task");
		while (page.isNotEmpty()) {
			List<Student> students = page.getItems();
			List<Long> userIds = new ArrayList<Long>(students.size());
			for (Student s : students) {
				userIds.add(s.getId());
			}

			Map<Long, UserTaskLog> logMap = zycUserTaskLogService.mgetByCodeAndUsers(userIds, getCode());
			for (long userId : userIds) {
				UserTaskLog log = logMap.get(userId);
				Credential credential = credentialService.getCredentialByUserId(Product.YOOMATH,
						CredentialType.QQ, userId);

				// 以前没有任务数据
				if (log == null) {
					UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
					// 已经绑定过
					if (credential != null) {
						List<CoinsLog> coinsLogs = coinsLogService.findLogs(121, userId, null);
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
					// 以前就有数据
					if (log.getStatus() == UserTaskLogStatus.DISABLED) {
						UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
						form.setId(log.getId());
						// 分为以前已经领取过奖励、还有一种未领取奖励
						List<CoinsLog> coinsLogs = coinsLogService.findLogs(getCode(), userId, null);
						if (CollectionUtils.isEmpty(coinsLogs)) {
							if (credential != null) {
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

			logger.info("before the {} student has inited data", page.getLast().getId());
			page =  studentService.query(CP.cursor(page.getLast().getId(), FETCH_SIZE));
		}

		logger.info("begin update task status to OPEN");
		zycUserTaskService.completeInitData(getCode());
		logger.info("end init qq user_task data");
	}

	public int getCode() {
		return 101000003;
	}
}
