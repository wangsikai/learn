package com.lanking.uxb.service.honor.api.impl.processor.growth;

import java.util.Date;

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
 * 在线时长超过30分钟 每人每天奖励5成长值
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月11日 下午6:04:45
 */
@Component
public class OnlineTimeEnoughGrowthProcessor extends AbstractGrowthProcessor {

	@Autowired
	private GrowthCacheService growthCacheService;
	@Autowired
	private GrowthLogService growthLogService;

	@Override
	public GrowthAction getAction() {
		return GrowthAction.ONLINE_TIME_ENOUGH;
	}

	@Transactional
	@Override
	public GrowthLog process(GrowthLog growthLog, boolean isUpgrade) {
		String todayKey = growthCacheService.getTodayKey(getAction(), growthLog.getUserId());
		long count = growthCacheService.get(todayKey);
		if (count == -1) {
			growthLog.setCreateAt(new Date());
			growthLog.setRuleCode(getGrowthRule().getCode());
			growthLog.setGrowthValue(getGrowthRule().getGrowthValue());
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
