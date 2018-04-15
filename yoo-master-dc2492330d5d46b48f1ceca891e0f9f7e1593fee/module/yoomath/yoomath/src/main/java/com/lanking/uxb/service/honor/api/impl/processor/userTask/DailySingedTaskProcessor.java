package com.lanking.uxb.service.honor.api.impl.processor.userTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
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
 * 每日任务： 每日签到 code == 101010001
 *
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Component
public class DailySingedTaskProcessor extends AbstractUserTaskProcessor {
	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private UserUpLevelService upLevelService;
	@Autowired
	private SignCountService signCountService;
	@Autowired
	private UserTaskStarLogService userTaskStarLogService;

	public void process(UserTask userTask, long userId, Map<String, Object> params) {
		Date date = new Date();
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
		Date yesterday = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
			yesterday = DateUtils.addDays(date, -1);
		} catch (ParseException e) {
		}

		UserTaskLog log = userTaskLogService.findByCodeAndUser(userTask.getCode(), userId, date);
		UserTaskLog yesterdayLog = userTaskLogService.findByCodeAndUser(userTask.getCode(), userId, yesterday);
		UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
		if (null == log) {

			UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();

			List<Integer> itemCoins = cfg.getItemCoins();
			List<Integer> itemStars = cfg.getItemStar();
			List<Integer> itemGrowth = cfg.getItemGrowth();

			List<Map<String, Object>> itemMaps = new ArrayList<Map<String, Object>>(2);
			int coins = 0, growth = 0, stars = 0;
			long signDay = 1;

			if (yesterdayLog == null) {
				if (itemCoins != null) {
					coins = itemCoins.get(0);
				}
				if (itemGrowth != null) {
					growth = itemGrowth.get(0);
				}
				if (itemStars != null) {
					stars = itemStars.get(0);
				}
				Map<String, Object> m = new HashMap<String, Object>(2);
				m.put("completeAt", System.currentTimeMillis());
				m.put("index", 1);

				itemMaps.add(m);
			} else {
				String content = yesterdayLog.getContent();
				JSONObject contentJsonObj = JSONObject.parseObject(content);
				signDay = contentJsonObj.getLong("signDay") + 1;
				for (Integer c : itemCoins) {
					coins += c;
				}
				for (Integer g : itemGrowth) {
					growth += g;
				}
				for (Integer s : itemStars) {
					stars += s;
				}

				// 两颗星 completeAt 用于点亮星星
				Map<String, Object> m1 = new HashMap<String, Object>(2);
				m1.put("completeAt", System.currentTimeMillis());
				m1.put("index", 1);
				itemMaps.add(m1);

				Map<String, Object> m2 = new HashMap<String, Object>(2);
				m2.put("completeAt", System.currentTimeMillis());
				m2.put("index", 2);
				itemMaps.add(m2);
			}

			JSONObject contentJsonObj = new JSONObject();
			contentJsonObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
			contentJsonObj.put("completeAt", System.currentTimeMillis());
			contentJsonObj.put("items", itemMaps);
			// 连续签到天数
			contentJsonObj.put("signDay", signDay);
			form.setUserId(userId);
			form.setContent(contentJsonObj.toJSONString());
			form.setTaskCode(userTask.getCode());
			form.setDate(date);
			form.setStatus(UserTaskLogStatus.RECEIVE);
			form.setCompleteAt(new Date());
			form.setCoins(coins);
			form.setStar(stars);
			form.setGrowth(growth);
			form.setType(userTask.getType());

			log = userTaskLogService.update(form);

			// 直接领取
			boolean isClient = false; // 是否是客户端调用
			if (params != null && params.get("isClient") != null) {
				isClient = Boolean.parseBoolean(params.get("isClient").toString());
			}

			if (isClient) {
				upLevelService.setUserHonorSinged(userId, growth, coins, form);
			} else {
				upLevelService.setUserHonorWeb(userId, growth, coins, form);
			}

			signCountService.sign(userTask.getCode(), userId, params);

			// 签到星级日志
			int star = log.getStar();
			if (star > 0) {
				userTaskStarLogService.update(log.getUserId(), star, null, new Date());
			}
		}
	}

	public boolean accept(int code) {
		return getCode() == code;
	}

	public int getCode() {
		return 101010001;
	}
}
