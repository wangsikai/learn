package com.lanking.uxb.service.honor.api.impl.processor.userTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractUserTaskProcessor;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;

/**
 * 暂不使用
 * 
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Component
@Deprecated
public class CheckInAchievementTaskProcessor extends AbstractUserTaskProcessor {
	@Autowired
	private UserTaskLogService userTaskLogService;

	@Override
	public boolean accept(int code) {
		return code == 101020019;
	}

	@Override
	public UserTask getUserTask(int code, long userId, Map<String, Object> params) {
		int days = (int) params.get("days");
		if (days <= 30) {
			return super.getUserTask(101020019);
		} else if (days <= 60) {
			return super.getUserTask(101020020);
		}

		return super.getUserTask(101020021);
	}

	@Override
	public int getCode() {
		return 101020019;
	}

	@Override
	public void process(UserTask userTask, long userId, Map<String, Object> params) {
		List<Integer> codes = new ArrayList<Integer>(3);
		codes.add(101020019);
		codes.add(101020020);
		codes.add(101020021);

		int days = (int) params.get("days");
		List<UserTaskLog> logs = userTaskLogService.findByCodeAndUser(codes, userId);

		UserTaskRuleCfg cfg = null;
		if (CollectionUtils.isEmpty(logs)) {
			UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
			if (days < 30) {
				userTask = super.getUserTask(101020019);

				cfg = userTask.getUserTaskRuleCfg();

				form.setUserId(userId);
				form.setStatus(UserTaskLogStatus.TASKING);
				form.setType(userTask.getType());
				form.setCoins(cfg.getCoinsValue());
				form.setGrowth(cfg.getGrowthValue());
				form.setTaskCode(userTask.getCode());
				form.setType(userTask.getType());
				form.setDate(new Date());

				JSONObject contentObj = new JSONObject();
				contentObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
				form.setContent(contentObj.toJSONString());

				userTaskLogService.update(form);
			} else if (days >= 30 && days < 60) {
				userTask = super.getUserTask(101020019);
				cfg = userTask.getUserTaskRuleCfg();

				form.setUserId(userId);
				form.setStatus(UserTaskLogStatus.COMPLETE);
				form.setType(userTask.getType());
				form.setCoins(cfg.getCoinsValue());
				form.setGrowth(cfg.getGrowthValue());
				form.setTaskCode(userTask.getCode());
				form.setType(userTask.getType());
				form.setDate(new Date());

				JSONObject contentObj = new JSONObject();
				contentObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
				form.setContent(contentObj.toJSONString());

				userTaskLogService.update(form);

				userTask = super.getUserTask(101020020);

				cfg = userTask.getUserTaskRuleCfg();

				form.setUserId(userId);
				form.setStatus(UserTaskLogStatus.TASKING);
				form.setType(userTask.getType());
				form.setCoins(cfg.getCoinsValue());
				form.setGrowth(cfg.getGrowthValue());
				form.setTaskCode(userTask.getCode());
				form.setType(userTask.getType());
				form.setDate(new Date());

				JSONObject contentObj2 = new JSONObject();
				contentObj2.put("content", userTask.getType().getName() + ":" + userTask.getName());
				form.setContent(contentObj2.toJSONString());

				userTaskLogService.update(form);
			}

		}
	}
}
