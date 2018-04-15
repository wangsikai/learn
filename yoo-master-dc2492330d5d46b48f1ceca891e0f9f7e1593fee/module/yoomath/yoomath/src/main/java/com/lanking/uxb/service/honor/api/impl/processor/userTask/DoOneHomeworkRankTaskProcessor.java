package com.lanking.uxb.service.honor.api.impl.processor.userTask;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractUserTaskProcessor;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;

/**
 * 单次作业排名前5
 *
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Component
public class DoOneHomeworkRankTaskProcessor extends AbstractUserTaskProcessor {
	@Autowired
	private UserTaskLogService userTaskLogService;
	private static final int COMMIT_COUNT = 20;
	private static final int RANK = 5;

	@Override
	public boolean accept(int code) {
		return getCode() == code;
	}

	@Override
	public int getCode() {
		return 101020012;
	}

	public void process(UserTask userTask, long userId, Map<String, Object> params) {
		int commitCount = (int) params.get("commitCount");
		Integer rank = params.get("rank") == null ? null : Integer.parseInt(String.valueOf(params.get("rank")));

		UserTaskLog userTaskLog = userTaskLogService.findByCodeAndUser(userTask.getCode(), userId);
		// 只可以领取一次
		if (userTaskLog != null) {
			return;
		}
		UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();
		int coins = cfg.getCoinsValue();
		int growth = cfg.getGrowthValue();

		UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
		if (commitCount >= COMMIT_COUNT && rank != null && rank <= RANK) {
			form.setCoins(coins);
			form.setGrowth(growth);
			form.setStatus(UserTaskLogStatus.COMPLETE);
			form.setCompleteAt(new Date());
			form.setUserId(userId);

			JSONObject contentJsonObj = new JSONObject();
			contentJsonObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
			contentJsonObj.put("completeAt", System.currentTimeMillis());
			contentJsonObj.put("items", Collections.EMPTY_LIST);
			contentJsonObj.put("doCount", 1);
			contentJsonObj.put("allCount", 1);
			form.setTaskCode(userTask.getCode());
			form.setContent(contentJsonObj.toJSONString());
			form.setType(userTask.getType());

			userTaskLogService.update(form);

		}
	}
}
