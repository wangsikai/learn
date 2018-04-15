package com.lanking.uxb.service.honor.api.impl.processor.userTask;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractUserTaskProcessor;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Component
public class HomeworkImproveRankTaskProcessor extends AbstractUserTaskProcessor {
	@Autowired
	private UserTaskLogService userTaskLogService;

	@Override
	public boolean accept(int code) {
		return getCode() == code;
	}

	@Override
	public int getCode() {
		return 101020015;
	}

	@Override
	public void process(UserTask userTask, long userId, Map<String, Object> params) {
		UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();

		int coins = cfg.getCoinsValue();
		int growth = cfg.getGrowthValue();

		UserTaskLog userTaskLog = userTaskLogService.findByCodeAndUser(userTask.getCode(), userId);

		if (userTaskLog == null) {
			UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();

			form.setUserId(userId);
			form.setDate(new Date());
			form.setStatus(UserTaskLogStatus.COMPLETE);
			form.setCompleteAt(new Date());
			form.setTaskCode(userTask.getCode());
			form.setGrowth(growth);
			form.setCoins(coins);
			form.setType(userTask.getType());

			JSONObject contentObj = new JSONObject();
			contentObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
			contentObj.put("completeAt", System.currentTimeMillis());

			List<Map<String, Object>> itemMaps = new ArrayList<Map<String, Object>>(1);
			Map<String, Object> m = new HashMap<String, Object>(4);
			m.put("index", 1);
			m.put("coins", coins);
			m.put("growth", growth);
			m.put("completeAt", System.currentTimeMillis());
			itemMaps.add(m);
			contentObj.put("items", itemMaps);
			form.setContent(contentObj.toJSONString());

			userTaskLogService.update(form);
		}
	}
}
