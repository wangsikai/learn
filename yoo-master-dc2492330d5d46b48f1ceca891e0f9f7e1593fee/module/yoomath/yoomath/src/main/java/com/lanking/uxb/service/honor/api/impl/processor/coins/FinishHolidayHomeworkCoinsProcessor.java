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
import com.lanking.uxb.service.honor.api.CoinsRuleService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;
import com.lanking.uxb.service.honor.cache.CoinsCacheService;

/**
 * 完成假期作业金币相关
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月21日 下午4:45:14
 */
@Component
public class FinishHolidayHomeworkCoinsProcessor extends AbstractCoinsProcessor {

	@Autowired
	private CoinsRuleService coinsRuleService;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private CoinsCacheService coinsCacheService;

	@Override
	public CoinsAction getAction() {
		return CoinsAction.FINISH_HOLIDAY_HOMEWORK;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		String timesKey = coinsCacheService.getTimesKey(getAction(), coinsLog.getUserId());
		long count = coinsCacheService.get(timesKey);
		JSONObject jsonRule = new JSONObject(getCoinsRule().getRule());
		if (count < jsonRule.getInt("max")) {
			coinsLog.setCreateAt(new Date());
			coinsLog.setRuleCode(getCoinsRule().getCode());
			coinsLog.setCoinsValue(getCoinsRule().getCoinsValue());
			coinsLog.setStatus(Status.ENABLED);
			coinsLogService.save(coinsLog);
			update(coinsLog);
			coinsCacheService.incr(timesKey);
		}
		return coinsLog;
	}
}
