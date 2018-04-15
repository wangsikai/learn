package com.lanking.uxb.service.honor.api.impl.processor.coins;

import java.util.Date;

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
 * 学生在线时长超过30分钟 5金币
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月11日 下午6:09:11
 */
@Component
public class OnlineTimeEnoughCoinsProcessor extends AbstractCoinsProcessor {

	@Autowired
	private CoinsCacheService coinsCacheService;
	@Autowired
	private CoinsLogService coinsLogService;

	@Override
	public CoinsAction getAction() {
		return CoinsAction.ONLINE_TIME_ENOUGH;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		String todayKey = coinsCacheService.getTodayKey(getAction(), coinsLog.getUserId());
		long count = coinsCacheService.get(todayKey);
		if (count == -1) {
			coinsLog.setCreateAt(new Date());
			coinsLog.setRuleCode(getCoinsRule().getCode());
			coinsLog.setCoinsValue(getCoinsRule().getCoinsValue());
			coinsLog.setStatus(Status.ENABLED);
			coinsLogService.save(coinsLog);
			update(coinsLog);
			coinsCacheService.incr(todayKey);
		}
		return coinsLog;
	}
}
