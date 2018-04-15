package com.lanking.uxb.service.honor.api.impl.processor.userTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.lanking.uxb.service.honor.api.UserTaskStarLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractUserTaskProcessor;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;

/**
 * 每日任务： 完成作业 code == 101010002
 *
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Component
public class FinishHomeworkTaskProcessor extends AbstractUserTaskProcessor {
	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private UserUpLevelService upLevelService;
	@Autowired
	private UserTaskStarLogService userTaskStarLogService;

	public void process(UserTask userTask, long userId, Map<String, Object> params) {
		Date date = new Date();
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
		} catch (ParseException e) {
		}

		UserTaskLog log = userTaskLogService.findByCodeAndUser(userTask.getCode(), userId, date);
		// web端处理成长值任务
		boolean isClient = false; // 是否是客户端调用
		if (params != null && params.get("isClient") != null) {
			isClient = Boolean.parseBoolean(params.get("isClient").toString());
		}
		if (log == null) {
			UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
			UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();
			form.setStatus(isClient ? UserTaskLogStatus.COMPLETE : UserTaskLogStatus.RECEIVE);
			form.setCompleteAt(new Date());
			form.setUserId(userId);
			form.setTaskCode(userTask.getCode());
			form.setCoins(cfg.getCoinsValue());
			form.setGrowth(cfg.getGrowthValue());
			form.setStar(cfg.getActiveStar());

			JSONObject contentJsonObj = new JSONObject();
			contentJsonObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
			contentJsonObj.put("completeAt", System.currentTimeMillis());
			// 完成任务设置items
			List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("index", 1);
			logMap.put("content", "完成1次");
			logMap.put("completeAt", System.currentTimeMillis());
			items.add(logMap);
			contentJsonObj.put("items", items);
			// 添加star
			Integer itemStar = cfg.getActiveStar();
			form.setContent(contentJsonObj.toJSONString());

			form.setType(userTask.getType());
			form.setDate(date);

			log = userTaskLogService.update(form);

			if (!isClient) {
				upLevelService.setUserHonorWeb(userId, cfg.getGrowthValue(), cfg.getCoinsValue(), form);

				// 完成作业web端自动领取加星级日志
				int star = log.getStar();
				if (star > 0) {
					userTaskStarLogService.update(log.getUserId(), star, null, new Date());
				}
			}
		}
	}

	public int getCode() {
		return 101010002;
	}

	public boolean accept(int code) {
		return getCode() == code;
	}
}
