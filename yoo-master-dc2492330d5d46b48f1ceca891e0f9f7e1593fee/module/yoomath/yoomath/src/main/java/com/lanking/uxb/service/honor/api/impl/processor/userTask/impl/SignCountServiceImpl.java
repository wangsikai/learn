/**
 * 
 */
package com.lanking.uxb.service.honor.api.impl.processor.userTask.impl;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.api.UserUpLevelService;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.api.impl.processor.userTask.SignCountService;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 *
 */
@Service
@Transactional(readOnly = true)
public class SignCountServiceImpl implements SignCountService {

	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private UserUpLevelService upLevelService;

	@Transactional
	@Override
	public void sign(long checkCode, long userId, Map<String, Object> params) {
		Long checkCount = userTaskLogService.count(checkCode, userId);
		int code = 0;
		int allCount = 0;
		if (checkCount <= 30) {
			allCount = 30;
			code = 101020019;
		} else if (checkCount > 30 && checkCount <= 60) {
			allCount = 60;
			code = 101020020;
		} else if (checkCount > 60 && checkCount <= 90) {
			allCount = 90;
			code = 101020021;
		}
		if (code == 0) {
			return;
		}
		UserTaskLog log = userTaskLogService.findByCodeAndUser(code, userId);
		UserTask userTask = userTaskService.get(code);
		UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();
		int growth = cfg.getGrowthValue() == -1 ? 0 : cfg.getGrowthValue();
		int coins = cfg.getCoinsValue() == -1 ? 0 : cfg.getCoinsValue();
		UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
		boolean isClient = true; // 是否是客户端调用
		if (params != null && params.get("isClient") != null) {
			isClient = Boolean.parseBoolean(params.get("isClient").toString());
		}
		if (log == null) {
			form.setType(userTask.getType());
			form.setStatus(UserTaskLogStatus.TASKING);
			form.setTaskCode(userTask.getCode());
			form.setUserId(userId);
			JSONObject contentJsonObj = new JSONObject();
			contentJsonObj.put("doCount", checkCount);
			contentJsonObj.put("items", Collections.EMPTY_LIST);
			contentJsonObj.put("allCount", allCount);
			form.setContent(contentJsonObj.toJSONString());
			userTaskLogService.update(form);
			// 如果为30天全部初始化
			if (code == 101020019) {
				// 60天
				contentJsonObj.put("doCount", 30);
				contentJsonObj.put("items", Collections.EMPTY_LIST);
				contentJsonObj.put("allCount", 60);
				form.setContent(contentJsonObj.toJSONString());
				form.setTaskCode(101020020);
				userTaskLogService.update(form);
				// 90天
				contentJsonObj.put("doCount", 60);
				contentJsonObj.put("items", Collections.EMPTY_LIST);
				contentJsonObj.put("allCount", 90);
				form.setContent(contentJsonObj.toJSONString());
				form.setTaskCode(101020021);
				userTaskLogService.update(form);
			}
		} else {
			String content = log.getContent();
			JSONObject contentJsonObj = JSONObject.parseObject(content);
			Long completeAt = contentJsonObj.getLong("completeAt");
			if (completeAt == null && checkCount >= allCount) {
				// 首次完成任务
				contentJsonObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
				contentJsonObj.put("completeAt", System.currentTimeMillis());
				contentJsonObj.put("content", "完成1次");
				contentJsonObj.put("index", 1);
				contentJsonObj.put("doCount", checkCount);
				form.setCoins(coins);
				form.setGrowth(growth);
				if (isClient) {
					form.setStatus(UserTaskLogStatus.COMPLETE);
				} else {
					// web端达成任务后，需要直接领取
					form.setStatus(UserTaskLogStatus.RECEIVE);
					form.setTaskCode(log.getTaskCode());
					form.setUserId(userId);
					upLevelService.setUserHonorWeb(userId, growth, coins, form);
				}

			}
			if (completeAt == null) {
				contentJsonObj.put("doCount", checkCount);
				form.setContent(contentJsonObj.toJSONString());
				form.setId(log.getId());
				userTaskLogService.update(form);
			}

		}
	}
}
