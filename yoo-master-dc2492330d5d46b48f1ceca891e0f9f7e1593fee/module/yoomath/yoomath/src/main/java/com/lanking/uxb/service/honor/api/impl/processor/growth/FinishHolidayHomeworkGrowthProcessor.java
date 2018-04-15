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
import com.lanking.uxb.service.honor.api.GrowthRuleService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractGrowthProcessor;
import com.lanking.uxb.service.honor.cache.GrowthCacheService;

/**
 * 完成假期作业相关
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月21日 下午4:45:14
 */
@Component
public class FinishHolidayHomeworkGrowthProcessor extends AbstractGrowthProcessor {

	@Autowired
	private GrowthRuleService growthRuleService;
	@Autowired
	private GrowthLogService growthLogService;
	@Autowired
	private GrowthCacheService growthCacheService;

	@Override
	public GrowthAction getAction() {
		return GrowthAction.FINISH_HOLIDAY_HOMEWORK;
	}

	@Transactional
	@Override
	public GrowthLog process(GrowthLog growthLog, boolean isUpgrade) {
		String timesKey = growthCacheService.getTimesKey(getAction(), growthLog.getUserId());
		long count = growthCacheService.get(timesKey);
		JSONObject jsonRule = new JSONObject(getGrowthRule().getRule());
		if (count < jsonRule.getInt("max")) {
			growthLog.setCreateAt(new Date());
			growthLog.setRuleCode(getGrowthRule().getCode());
			growthLog.setGrowthValue(getGrowthRule().getGrowthValue());
			growthLog.setStatus(Status.ENABLED);
			growthLogService.save(growthLog);
			UserHonor userHonor = update(growthLog);
			if (isUpgrade) {
				growthLog.setHonor(userHonor);
			}
			growthCacheService.incr(timesKey);
		}
		return growthLog;
	}
}
