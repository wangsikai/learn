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
 * 提交作业 成长值
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月10日 上午11:24:51
 */
@Component
public class SubmitHomeworkGrowthProcessor extends AbstractGrowthProcessor {

	@Autowired
	private GrowthCacheService growthCacheService;
	@Autowired
	private GrowthLogService growthLogService;

	@Override
	public GrowthAction getAction() {
		return GrowthAction.SUBMIT_HOMEWORK;
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
			if (growthLog.getGrowthValue() == -1) {
				growthLog.setGrowthValue(getGrowthRule().getGrowthValue());
			}
			growthLog.setStatus(Status.ENABLED);
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
