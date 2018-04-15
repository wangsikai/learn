package com.lanking.uxb.zycon.task.api.impl.init;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStatus;
import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;
import com.lanking.uxb.zycon.homework.api.ZycStudentService;
import com.lanking.uxb.zycon.task.api.ZycUserTaskLogService;
import com.lanking.uxb.zycon.task.api.ZycUserTaskService;
import com.lanking.uxb.zycon.task.form.ZycUserTaskForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Service
public class ZycUserTaskCompleteInfoInitService {
	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private ZycStudentService zycStudentService;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private ZycUserTaskService zycUserTaskService;
	@Autowired
	private ZycUserTaskLogService zycUserTaskLogService;
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

		while (page.isNotEmpty()) {
			List<Student> students = page.getItems();

			UserTask userTask = userTaskService.get(getCode());
			UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();

			boolean setAccountName = true, setName = false, setSex = false, setBirthDay = false,
			        setSchool = false, setYear = false;
			for (Student s : students) {
				setYear = s.getYear() != null && s.getYear() > 0;
				setName = StringUtils.isNotBlank(s.getName());
				setSchool = s.getSchoolId() != null && s.getSchoolId() > 0;
				setBirthDay = s.getBirthday() != null;
				setSex = s.getSex() != null && s.getSex() != Sex.UNKNOWN;

				UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
				UserTaskLog userTaskLog = zycUserTaskLogService.findByUserTaskAndUser(getCode(), s.getId());

				if (userTaskLog == null) {
					setContent(userTask, cfg, setAccountName, setName, setSex, setBirthDay,
					        setSchool, setYear, form);
					form.setType(userTask.getType());
					form.setDate(new Date());
					form.setUserId(s.getId());

					userTaskLogService.update(form);
				} else {
					CoinsLog coinsLog = coinsLogService.find(getCode(), s.getId(), null);
					form.setId(userTaskLog.getId());
					if (coinsLog != null) {
						form.setStatus(UserTaskLogStatus.RECEIVE);
					} else {
						if (StringUtils.isBlank(userTaskLog.getContent())) {
							form.setStatus(UserTaskLogStatus.TASKING);
						} else {
							JSONObject contentJsonObj = JSONObject.parseObject(userTaskLog.getContent());
							JSONArray itemArr = contentJsonObj.getJSONArray("items");
							if (itemArr == null || itemArr.size() < 6) {
								setContent(userTask, cfg, setAccountName, setName, setSex,
								        setBirthDay, setSchool, setYear, form);
							} else {
								form.setStatus(UserTaskLogStatus.COMPLETE);
							}
						}
					}

					userTaskLogService.update(form);
				}
			}

			pageable = CP.cursor(page.getLast().getId(), FETCH_SIZE);
			page = zycStudentService.query(pageable);
		}

		zycUserTaskService.completeInitData(getCode());
	}

	private void setContent(UserTask userTask, UserTaskRuleCfg cfg, boolean setAccountName,
	        boolean setName, boolean setSex, boolean setBirthDay, boolean setSchool,
	        boolean setYear, UserTaskLogUpdateForm form) {
		JSONObject jsonObject = new JSONObject();

		List<Map<String, Object>> itemMaps = new ArrayList<Map<String, Object>>(5);
		int coins = cfg.getCoinsValue();
		int growth = cfg.getGrowthValue();
		Map<String, Object> m = new HashMap<String, Object>(2);
		m.put("completeAt", System.currentTimeMillis());
		m.put("index", 1);
		itemMaps.add(m);
		if (setName) {
			m = new HashMap<String, Object>(2);
			m.put("completeAt", System.currentTimeMillis());
			m.put("index", 2);
			itemMaps.add(m);
		}
		if (setSex) {
			m = new HashMap<String, Object>(2);
			m.put("completeAt", System.currentTimeMillis());
			m.put("index", 3);
			itemMaps.add(m);
		}
		if (setBirthDay) {
			m = new HashMap<String, Object>(2);
			m.put("completeAt", System.currentTimeMillis());
			m.put("index", 4);
			itemMaps.add(m);
		}
		if (setSchool) {
			m = new HashMap<String, Object>(2);
			m.put("completeAt", System.currentTimeMillis());
			m.put("index", 5);
			itemMaps.add(m);
		}
		if (setYear) {
			m = new HashMap<String, Object>(2);
			m.put("completeAt", System.currentTimeMillis());
			m.put("index", 6);
			itemMaps.add(m);
		}

		form.setCoins(coins);
		form.setGrowth(growth);
		form.setTaskCode(userTask.getCode());

		jsonObject.put("items", itemMaps);
		if (setAccountName && setBirthDay && setSchool && setName && setSex && setYear) {
			jsonObject.put("completeAt", System.currentTimeMillis());
			jsonObject.put("content", userTask.getType().getName() + ":" + userTask.getName());
			form.setStatus(UserTaskLogStatus.COMPLETE);
		} else {
			form.setStatus(UserTaskLogStatus.TASKING);
		}

		form.setContent(jsonObject.toJSONString());
	}

	public int getCode() {
		return 101000001;
	}
}
