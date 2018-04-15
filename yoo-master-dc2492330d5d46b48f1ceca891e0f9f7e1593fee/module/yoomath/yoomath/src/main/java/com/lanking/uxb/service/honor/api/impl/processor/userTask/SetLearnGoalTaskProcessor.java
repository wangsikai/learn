package com.lanking.uxb.service.honor.api.impl.processor.userTask;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.uxb.service.honor.api.UserUpLevelService;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractUserTaskProcessor;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;

/**
 * 新手任务： 指定学习目标 code == 101000002
 *
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Component
public class SetLearnGoalTaskProcessor extends AbstractUserTaskProcessor {

	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private UserUpLevelService upLevelService;

	@Override
	public boolean accept(int code) {
		return getCode() == code;
	}

	@Override
	public int getCode() {
		return 101000002;
	}

	public void process(UserTask userTask, long userId, Map<String, Object> params) {
		UserTaskLog log = userTaskLogService.findByCodeAndUser(userTask.getCode(), userId);
		if (log == null) {
			UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();

			int coins = cfg.getCoinsValue();
			int growth = cfg.getGrowthValue();

			UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
			form.setUserId(userId);
			form.setTaskCode(userTask.getCode());
			form.setType(userTask.getType());
			form.setGrowth(growth);
			form.setCoins(coins);
			form.setStatus(UserTaskLogStatus.COMPLETE);
			form.setCompleteAt(new Date());

			JSONObject contentJsonObj = new JSONObject();
			contentJsonObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
			contentJsonObj.put("completeAt", System.currentTimeMillis());
			contentJsonObj.put("items", Collections.EMPTY_LIST);
			form.setContent(contentJsonObj.toJSONString());
			form.setDate(new Date());

			userTaskLogService.update(form);

			// web端处理成长值任务
			boolean isClient = false; // 是否是客户端调用
			if (params != null && params.get("isClient") != null) {
				isClient = Boolean.parseBoolean(params.get("isClient").toString());
			}
			if (!isClient) {
				upLevelService.setUserHonorWeb(userId, growth, coins, form);
			}
		}
	}

}
