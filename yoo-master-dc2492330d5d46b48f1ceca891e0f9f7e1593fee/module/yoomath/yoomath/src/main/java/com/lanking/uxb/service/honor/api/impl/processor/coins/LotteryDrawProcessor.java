package com.lanking.uxb.service.honor.api.impl.processor.coins;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;
import com.lanking.uxb.service.mall.cache.CoinsLotteryCacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 抽奖金币处理，抽中增加金币，及每次抽消耗的金币
 *
 * @author xinyu.zhou
 * @since 2.4.0
 */
@Component
public class LotteryDrawProcessor extends AbstractCoinsProcessor {
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private CoinsLotteryCacheService cacheService;

	@Override
	public CoinsAction getAction() {
		return CoinsAction.LOTTERY_DRAW;
	}

	@Override
	@Transactional
	public CoinsLog process(CoinsLog coinsLog) {
		List<Boolean> userLottery = cacheService.getUserLottery(coinsLog.getUserId());
		coinsLog.setCreateAt(new Date());
		coinsLog.setRuleCode(getCoinsRule().getCode());
		// 今天每几次抽奖
		coinsLog.setP1(String.valueOf(userLottery.size()));

		update(coinsLog);
		coinsLogService.save(coinsLog);
		return coinsLog;
	}
}
