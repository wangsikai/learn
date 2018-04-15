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
 * 每日任务： 完成错题练习 code == 101010003
 *
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Component
public class DoFallibleExerciseTaskProcessor extends AbstractUserTaskProcessor {
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
		boolean isClient = false; // 是否是客户端调用
		if (params != null && params.get("isClient") != null) {
			isClient = Boolean.parseBoolean(params.get("isClient").toString());
		}
		if (log == null) {
			UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
			UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();
			// web端暂时处理为已领取
			form.setStatus(isClient ? UserTaskLogStatus.COMPLETE : UserTaskLogStatus.RECEIVE);
			form.setCompleteAt(new Date());
			form.setUserId(userId);
			form.setTaskCode(userTask.getCode());
			form.setCoins(cfg.getCoinsValue());
			form.setGrowth(cfg.getGrowthValue());

			JSONObject contentJsonObj = new JSONObject();
			long completeAt = System.currentTimeMillis();
			contentJsonObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
			contentJsonObj.put("completeAt", completeAt);
			if (!isClient) {
				contentJsonObj.put("receiveAt", completeAt);
			}
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
			form.setStar(itemStar);

			log = userTaskLogService.update(form);

			// web端处理成长值任务

			if (!isClient) {
				upLevelService.setUserHonorWeb(userId, cfg.getGrowthValue(), cfg.getCoinsValue(), form);

				// 完成做题练习web端自动领取加星级日志
				int star = log.getStar();
				if (star > 0) {
					userTaskStarLogService.update(log.getUserId(), star, null, new Date());
				}
			}
		}
	}

	public int getCode() {
		return 101010003;
	}

	public boolean accept(int code) {
		return getCode() == code;
	}
}
