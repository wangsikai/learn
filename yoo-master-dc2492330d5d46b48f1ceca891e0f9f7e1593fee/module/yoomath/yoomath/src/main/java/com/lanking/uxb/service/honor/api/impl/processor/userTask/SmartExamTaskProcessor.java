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
 * 每日任务：
 *
 * 智能出卷 code == 101010006
 *
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Component
public class SmartExamTaskProcessor extends AbstractUserTaskProcessor {
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
		if (null == log) {
			long now = System.currentTimeMillis();

			UserTaskLogUpdateForm updateForm = new UserTaskLogUpdateForm();
			UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();

			Integer rightRate = Integer.valueOf(params.get("rightRate").toString());
			Integer growth = cfg.getGrowthValue();
			List<Integer> itemCoins = cfg.getItemCoins();
			List<Integer> itemStar = cfg.getItemStar();
			List<String> itemContents = cfg.getItems();
			List<String> itemsThreshold = cfg.getItemsThreshold();

			updateForm.setUserId(userId);
			updateForm.setTaskCode(userTask.getCode());

			JSONObject content = new JSONObject();
			content.put("completeAt", now);
			content.put("content", new StringBuffer().append(userTask.getType().getName()).append(":")
					.append(userTask.getNote()).toString());

			List<Map<String, Object>> items = new ArrayList<Map<String, Object>>(itemContents.size());
			int index = 1;

			int coins = 0;
			int stars = 0;
			for (String itemThreshold : itemsThreshold) {
				int rightRateThreshold = Integer.valueOf(itemThreshold);

				if (rightRate >= rightRateThreshold) {
					Map<String, Object> logMap = new HashMap<String, Object>(3);

					logMap.put("index", index);
					logMap.put("content", itemContents.get(index - 1));
					logMap.put("completeAt", now);

					items.add(logMap);

					coins += itemCoins.get(index - 1) == null ? 0 : itemCoins.get(index - 1);
					stars += itemStar.get(index - 1) == null ? 0 : itemStar.get(index - 1);
				}

				index++;
			}

			content.put("items", items);

			updateForm.setContent(content.toJSONString());
			updateForm.setCoins(coins);
			updateForm.setStar(stars);
			updateForm.setGrowth(growth);
			updateForm.setType(userTask.getType());
			updateForm.setDate(date);
			updateForm.setStatus(UserTaskLogStatus.COMPLETE);
			updateForm.setCompleteAt(new Date());

			log = userTaskLogService.update(updateForm);

			// web端处理成长值任务
			boolean isClient = false; // 是否是客户端调用
			if (params != null && params.get("isClient") != null) {
				isClient = Boolean.parseBoolean(params.get("isClient").toString());
			}
			if (!isClient) {
				upLevelService.setUserHonorWeb(userId, growth, coins, updateForm);

				// 智能出卷web端自动领取加星级日志
				int star = log.getStar();
				if (star > 0) {
					userTaskStarLogService.update(log.getUserId(), star, null, new Date());
				}
			}
		}
	}

	public int getCode() {
		return 101010006;
	}

	public boolean accept(int code) {
		return getCode() == code;
	}

}
