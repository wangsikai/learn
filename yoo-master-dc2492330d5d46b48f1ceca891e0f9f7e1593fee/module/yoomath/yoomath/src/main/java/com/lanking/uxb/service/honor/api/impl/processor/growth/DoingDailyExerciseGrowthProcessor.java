package com.lanking.uxb.service.honor.api.impl.processor.growth;

import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.honor.api.GrowthLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractGrowthProcessor;
import com.lanking.uxb.service.honor.cache.GrowthCacheService;

/**
 * 每日作业 成长值
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月10日 上午11:26:17
 */
@Component
public class DoingDailyExerciseGrowthProcessor extends AbstractGrowthProcessor {

	@Autowired
	private GrowthCacheService growthCacheService;
	@Autowired
	private GrowthLogService growthLogService;

	@Override
	public GrowthAction getAction() {
		return GrowthAction.DOING_DAILY_EXERCISE;
	}

	@Transactional
	@Override
	public GrowthLog process(GrowthLog growthLog, boolean isUpgrade) {
		String todayKey = growthCacheService.getTodayKey(getAction(), growthLog.getUserId());
		long count = growthCacheService.get(todayKey);
		JSONObject jsonRule = new JSONObject(getGrowthRule().getRule());
		if (count < jsonRule.getInt("max")) {
			growthLog.setCreateAt(new Date());
			growthLog.setRuleCode(getGrowthRule().getCode());
			growthLog.setGrowthValue(getGrowthRule().getGrowthValue());
			growthLog.setStatus(Status.ENABLED);
			// 标记此题是今天做的第几题
			String p = count == -1 ? "1" : ((Long) (count + 1)).toString();
			growthLog.setP1(p);
			// 题目ID
			growthLog.setP2(((Long) growthLog.getBizId()).toString());
			growthLogService.save(growthLog);
			UserHonor userHonor = update(growthLog);
			if (isUpgrade) {
				growthLog.setHonor(userHonor);
			}
			growthCacheService.incr(todayKey);
		}
		return growthLog;
	}
}
