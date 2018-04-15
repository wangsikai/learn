package com.lanking.uxb.service.honor.api.impl.processor.coins;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;

/**
 * 活动抽奖获得金币
 *
 * @author xinyu.zhou
 * @since 2.4.0
 */
@Component
public class LotteryActivityEarnCoinsProcessor extends AbstractCoinsProcessor {

	@Autowired
	private CoinsLogService coinsLogService;

	@Override
	public CoinsAction getAction() {
		return CoinsAction.LOTTERY_ACTIVITY_EARN_COINS;
	}

	@Override
	@Transactional
	public CoinsLog process(CoinsLog coinsLog) {
		coinsLog.setCreateAt(new Date());
		coinsLog.setRuleCode(getCoinsRule().getCode());

		coinsLog.setP1(((Long) coinsLog.getBizId()).toString());

		update(coinsLog);

		coinsLogService.save(coinsLog);

		return coinsLog;
	}
}
