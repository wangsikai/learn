package com.lanking.uxb.service.honor.api.impl.processor.userTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
 * 新手任务： 绑定微信 code == 101000004
 *
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Component
public class BindWeChatTaskProcessor extends AbstractUserTaskProcessor {
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
		return 101000004;
	}

	public void process(UserTask userTask, long userId, Map<String, Object> params) {
		UserTaskLog log = userTaskLogService.findByCodeAndUser(userTask.getCode(), userId);
		if (null == log) {
			UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
			UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();

			boolean isClient = false; // 是否是客户端调用
			if (params != null && params.get("isClient") != null) {
				isClient = Boolean.parseBoolean(params.get("isClient").toString());
			}

			int coins = cfg.getCoinsValue();
			int growth = cfg.getGrowthValue();

			// web端暂时处理为已领取
			long completeAt = System.currentTimeMillis();
			form.setStatus(isClient ? UserTaskLogStatus.COMPLETE : UserTaskLogStatus.RECEIVE);
			form.setCompleteAt(new Date());
			form.setUserId(userId);
			form.setCoins(coins);
			form.setGrowth(growth);
			form.setTaskCode(userTask.getCode());

			JSONObject contentJsonObj = new JSONObject();
			contentJsonObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
			contentJsonObj.put("completeAt", completeAt);
			if (!isClient) {
				contentJsonObj.put("receiveAt", completeAt);
			}
			List<Map<String, Object>> itemMaps = new ArrayList<Map<String, Object>>(5);
			Map<String, Object> itemsMap = new HashMap<String, Object>(5);
			itemsMap.put("completeAt", completeAt);
			itemsMap.put("index", 1);
			itemMaps.add(itemsMap);
			contentJsonObj.put("items", itemMaps);
			contentJsonObj.put("isClient", isClient);

			form.setContent(contentJsonObj.toJSONString());
			form.setDate(new Date());
			form.setType(userTask.getType());

			userTaskLogService.update(form);
			// web端处理成长值任务
			if (!isClient) {
				upLevelService.setUserHonorWeb(userId, growth, coins, form);
			}
		}
	}
}
