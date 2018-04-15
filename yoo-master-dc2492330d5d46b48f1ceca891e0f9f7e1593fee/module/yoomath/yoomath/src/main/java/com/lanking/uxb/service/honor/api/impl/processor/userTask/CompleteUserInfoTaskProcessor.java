package com.lanking.uxb.service.honor.api.impl.processor.userTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.honor.api.UserUpLevelService;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractUserTaskProcessor;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;
import com.lanking.uxb.service.user.api.StudentService;

/**
 * 新手任务： 完善个人信息 code == 101000001
 *
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Component
public class CompleteUserInfoTaskProcessor extends AbstractUserTaskProcessor {
	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private UserUpLevelService upLevelService;

	@Override
	public boolean accept(int code) {
		return getCode() == code;
	}

	@Override
	public int getCode() {
		return 101000001;
	}

	@Override
	public void process(UserTask userTask, long userId, Map<String, Object> params) {
		UserTaskLog log = userTaskLogService.findByCodeAndUser(userTask.getCode(), userId);
		UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
		if (log == null) {
			log = new UserTaskLog();
		} else {
			form.setId(log.getId());
		}
		if (log.getStatus() != UserTaskLogStatus.RECEIVE) {

			Student student = (Student) studentService.getUser(UserType.STUDENT, userId);

			// "填写用户名","填写真实姓名","填写性别","填写生日","填写学校","填写入学年份"
			boolean setAccountName = true;
			boolean setYear = student.getYear() != null && student.getYear() > 0;
			boolean setBirthDay = student.getBirthday() != null;
			boolean setSex = student.getSex() != null && student.getSex() != Sex.UNKNOWN;
			boolean setSchool = student.getSchoolId() != null && student.getSchoolId() > 0;
			boolean setName = StringUtils.isNotBlank(student.getName());

			UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();

			JSONObject jsonObject = new JSONObject();

			List<Map<String, Object>> itemMaps = new ArrayList<Map<String, Object>>(5);
			Map<String, Object> itemsMap = new HashMap<String, Object>(5);
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
				form.setCompleteAt(new Date());
			} else {
				form.setStatus(UserTaskLogStatus.TASKING);
			}

			form.setContent(jsonObject.toJSONString());
			form.setType(userTask.getType());
			form.setDate(new Date());
			form.setUserId(userId);
			boolean isClient = false; // 是否是客户端调用
			if (params != null && params.get("isClient") != null) {
				isClient = Boolean.parseBoolean(params.get("isClient").toString());
			}
			if (!isClient && form.getStatus() == UserTaskLogStatus.COMPLETE) {
				form.setStatus(UserTaskLogStatus.RECEIVE);
			}
			userTaskLogService.update(form);
			// web端处理成长值任务
			if (!isClient && form.getStatus() == UserTaskLogStatus.RECEIVE) {
				upLevelService.setUserHonorWeb(userId, growth, coins, form);
			}
		}

	}
}
