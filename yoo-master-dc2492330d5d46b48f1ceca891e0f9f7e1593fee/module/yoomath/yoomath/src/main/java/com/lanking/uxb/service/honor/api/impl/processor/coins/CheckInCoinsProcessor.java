package com.lanking.uxb.service.honor.api.impl.processor.coins;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;
import com.lanking.uxb.service.honor.cache.CoinsCacheService;

/**
 * 用户签到金币<br>
 * 每日签到+10,连续签到2天及以上+15.断了重新开始+10<br>
 * rule的格式:[10,15]
 * 
 * @since yoomath V1.8
 * @author wangsenhao
 *
 */
@Component
public class CheckInCoinsProcessor extends AbstractCoinsProcessor {

	@Autowired
	private CoinsCacheService coinsCacheService;
	@Autowired
	private CoinsLogService coinsLogService;

	Map<Integer, Integer> rule = null;

	List<Integer> ruleList = null;

	List<Integer> getRuleList() {
		if (ruleList == null) {
			ruleList = JSONArray.parseArray(getCoinsRule().getRule(), Integer.class);
		}
		return ruleList;
	}

	int getNextCoins(int point) {
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
	public CoinsAction getAction() {
		return CoinsAction.DAILY_CHECKIN;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		String todayKey = coinsCacheService.getTodayKey(getAction(), coinsLog.getUserId());
		int todayPoint = (int) coinsCacheService.get(todayKey);
		if (todayPoint == -1) {// 今天没有签到
			coinsLog.setCreateAt(new Date());
			coinsLog.setRuleCode(getCoinsRule().getCode());
			String yesterdayKey = coinsCacheService.getYesterdayKey(getAction(), coinsLog.getUserId());
			int yesterdayCoins = (int) coinsCacheService.get(yesterdayKey);
			coinsLog.setCoinsValue(getNextCoins(yesterdayCoins));
			coinsLog.setNextCoins(getNextCoins(coinsLog.getCoinsValue()));
			coinsLogService.save(coinsLog);
			update(coinsLog);
			coinsCacheService.incr(todayKey, coinsLog.getCoinsValue());
		} else {
			coinsLog.setNextCoins(getNextCoins(coinsLog.getCoinsValue()));
		}
		return coinsLog;
	}
}
