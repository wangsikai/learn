package com.lanking.uxb.service.honor.api.impl.processor.coins;

import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;
import com.lanking.uxb.service.honor.cache.CoinsCacheService;

/**
 * 每日练习+错题练习 金币
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月10日 上午11:24:51
 */
@Component
public class DoingDailyExerciseCoinsProcessor extends AbstractCoinsProcessor {

	@Autowired
	private CoinsCacheService coinsCacheService;
	@Autowired
	private CoinsLogService coinsLogService;

	@Override
	public CoinsAction getAction() {
		return CoinsAction.DOING_DAILY_EXERCISE;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		String todayKey = coinsCacheService.getTodayKey(getAction(), coinsLog.getUserId());
		long count = coinsCacheService.get(todayKey);
		JSONObject jsonRule = new JSONObject(getCoinsRule().getRule());
		if (count < jsonRule.getInt("max")) {
			coinsLog.setCreateAt(new Date());
			coinsLog.setRuleCode(getCoinsRule().getCode());
			coinsLog.setCoinsValue(getCoinsRule().getCoinsValue());
			coinsLog.setStatus(Status.ENABLED);
			// 标记此题是今天做的第几题
			String p = count == -1 ? "1" : ((Long) (count + 1)).toString();
			coinsLog.setP1(p);
			// 题目ID
			coinsLog.setP2(((Long) coinsLog.getBizId()).toString());
			coinsLogService.save(coinsLog);
			update(coinsLog);
			coinsCacheService.incr(todayKey);
		}
		return coinsLog;
	}
}
