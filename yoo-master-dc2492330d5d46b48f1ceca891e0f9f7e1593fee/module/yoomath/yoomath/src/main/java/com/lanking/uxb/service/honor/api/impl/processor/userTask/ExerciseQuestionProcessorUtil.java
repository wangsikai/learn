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
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;

/**
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Component
public class ExerciseQuestionProcessorUtil {
	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private UserUpLevelService upLevelService;

	public void process(UserTask userTask, long userId, Map<String, Object> params, int rewardQuestionCount) {
		// web端处理成长值任务
		boolean isClient = false; // 是否是客户端调用
		if (params != null && params.get("isClient") != null) {
			isClient = Boolean.parseBoolean(params.get("isClient").toString());
		}

		UserTaskLog log = userTaskLogService.findByCodeAndUser(userTask.getCode(), userId);

		UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
		form.setTaskCode(userTask.getCode());
		form.setType(userTask.getType());
		form.setUserId(userId);
		int questionCount = (int) params.get("questionCount");

		UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();
		int coins = cfg.getCoinsValue();
		int growth = cfg.getGrowthValue();

		if (log == null) {
			form.setStatus(UserTaskLogStatus.TASKING);
			JSONObject contentJsonObj = new JSONObject();
			contentJsonObj.put("doCount", questionCount);
			contentJsonObj.put("items", Collections.EMPTY_LIST);
			contentJsonObj.put("allCount", rewardQuestionCount);
			form.setContent(contentJsonObj.toJSONString());

			userTaskLogService.update(form);
		} else {
			String content = log.getContent();
			JSONObject contentJsonObj = JSONObject.parseObject(content);
			int doCount = contentJsonObj.getInteger("doCount");
			Long completeAt = contentJsonObj.getLong("completeAt");

			doCount += questionCount;
			if (completeAt == null && doCount >= rewardQuestionCount) {
				// 首次完成任务
				contentJsonObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
				contentJsonObj.put("completeAt", System.currentTimeMillis());
				form.setCoins(coins);
				form.setGrowth(growth);

				if (isClient) {
					form.setStatus(UserTaskLogStatus.COMPLETE);
					form.setCompleteAt(new Date());
				} else {
					// web端达成任务后，需要直接领取
					form.setStatus(UserTaskLogStatus.RECEIVE);
					upLevelService.setUserHonorWeb(userId, growth, coins, form);

					// web端自动领取时，需要判断前序任务（此处暂不删，可能会改回去）
					// if (userTask.getCode() == 101020017 || userTask.getCode()
					// == 101020018) {
					// UserTask userTask16 = userTaskService.get(101020016);
					// UserTaskLog log16 =
					// userTaskLogService.findByCodeAndUser(101020016, userId);
					// if (log16 != null && log16.getStatus() ==
					// UserTaskLogStatus.COMPLETE) {
					// UserTaskLogUpdateForm form16 = new
					// UserTaskLogUpdateForm();
					// form16.setId(log16.getId());
					// form16.setTaskCode(log16.getTaskCode());
					// form16.setUserId(userId);
					// form16.setStatus(UserTaskLogStatus.RECEIVE);
					// form16.setGrowth(userTask16.getUserTaskRuleCfg().getGrowthValue());
					// form16.setCoins(userTask16.getUserTaskRuleCfg().getCoinsValue());
					// userTaskLogService.update(form16);
					// upLevelService.setUserHonorWeb(userId, growth, coins,
					// form16);
					// }
					// }
					// if (userTask.getCode() == 101020018) {
					// UserTask userTask17 = userTaskService.get(101020017);
					// UserTaskLog log17 =
					// userTaskLogService.findByCodeAndUser(101020017, userId);
					// if (log17 != null && log17.getStatus() ==
					// UserTaskLogStatus.COMPLETE) {
					// UserTaskLogUpdateForm form17 = new
					// UserTaskLogUpdateForm();
					// form17.setId(log17.getId());
					// form17.setTaskCode(log17.getTaskCode());
					// form17.setUserId(userId);
					// form17.setStatus(UserTaskLogStatus.RECEIVE);
					// form17.setGrowth(userTask17.getUserTaskRuleCfg().getGrowthValue());
					// form17.setCoins(userTask17.getUserTaskRuleCfg().getCoinsValue());
					// userTaskLogService.update(form17);
					// upLevelService.setUserHonorWeb(userId, growth, coins,
					// form17);
					// }
					// }
				}
			} else if (completeAt == null && doCount < rewardQuestionCount) {
				// 还未完成任务
				form.setStatus(UserTaskLogStatus.TASKING);
			}

			// 只有未完成的任务才更新
			if (completeAt == null) {
				contentJsonObj.put("doCount", doCount);
				form.setContent(contentJsonObj.toJSONString());
				form.setId(log.getId());
				userTaskLogService.update(form);
			}
		}
	}
}
