package com.lanking.uxb.service.honor.api.impl.processor.growth;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.uxb.service.honor.api.GrowthLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractGrowthProcessor;
import com.lanking.uxb.service.honor.cache.GrowthCacheService;

/**
 * 用户签到成长值<br>
 * 每日签到+10,连续签到2天及以上+15.断了重新开始+10<br>
 * rule的格式:[10,15]
 * 
 * @since yoomath V1.8
 * @author wangsenhao
 *
 */
@Component
public class CheckInGrowthProcessor extends AbstractGrowthProcessor {

	@Autowired
	private GrowthCacheService growthCacheService;
	@Autowired
	private GrowthLogService growthLogService;

	Map<Integer, Integer> rule = null;

	List<Integer> ruleList = null;

	List<Integer> getRuleList() {
		if (ruleList == null) {
			ruleList = JSONArray.parseArray(getGrowthRule().getRule(), Integer.class);
		}
		return ruleList;
	}

	int getNextGrowth(int point) {
		int index = 0;
		int size = getRuleList().size();
		for (int i = 0; i < size; i++) {
			if (ruleList.get(i) == point) {
				if (i == size - 1) {
					index = i;
				} else {
					index = i + 1;
				}
				break;
			}
		}
		return ruleList.get(index);
	}

	@Override
	public GrowthAction getAction() {
		return GrowthAction.DAILY_CHECKIN;
	}

	@Transactional
	@Override
	public GrowthLog process(GrowthLog growthLog, boolean isUpgrade) {
		String todayKey = growthCacheService.getTodayKey(getAction(), growthLog.getUserId());
		int todayPoint = (int) growthCacheService.get(todayKey);
		if (todayPoint == -1) {// 今天没有签到
			growthLog.setCreateAt(new Date());
			growthLog.setRuleCode(getGrowthRule().getCode());
			String yesterdayKey = growthCacheService.getYesterdayKey(getAction(), growthLog.getUserId());
			int yesterdayGrowth = (int) growthCacheService.get(yesterdayKey);
			growthLog.setGrowthValue(getNextGrowth(yesterdayGrowth));
			growthLog.setNextGrowth(getNextGrowth(growthLog.getGrowthValue()));
			String continuousKey = growthCacheService.getContinuousCheckInKey(getAction(), growthLog.getUserId());
			// 昨天没有签到过,连续签到次数置为1
			if (yesterdayGrowth == -1) {
				growthLog.setP1("1");
				growthCacheService.setValue(continuousKey, "1");
			} else {
				GrowthLog lastestGlog = growthLogService.getLastestCheckIn(growthLog.getUserId(),
						growthLog.getRuleCode());
				int p1 = Integer.parseInt(lastestGlog.getP1()) + 1;
				growthLog.setP1(String.valueOf(p1));
				growthCacheService.incr(continuousKey, 1);
			}
			growthLogService.save(growthLog);
			UserHonor userHonor = update(growthLog);
			if (isUpgrade) {
				growthLog.setHonor(userHonor);
			}
			growthCacheService.incr(todayKey, growthLog.getGrowthValue());
		} else {
			growthLog.setNextGrowth(getNextGrowth(growthLog.getGrowthValue()));
		}
		return growthLog;
	}
}
