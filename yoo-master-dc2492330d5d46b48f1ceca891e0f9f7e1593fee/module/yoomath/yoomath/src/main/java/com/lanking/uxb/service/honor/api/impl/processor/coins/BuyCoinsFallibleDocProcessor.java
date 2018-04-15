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
 * 购买错题文档.
 * 
 * @author wlche
 * @since web 2.0.3
 * 
 */
@Component
public class BuyCoinsFallibleDocProcessor extends AbstractCoinsProcessor {
	@Autowired
	private CoinsLogService coinsLogService;

	@Override
	public CoinsAction getAction() {
		return CoinsAction.BUY_COINS_FALLIBLE_DOC;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		coinsLog.setCreateAt(new Date());
		coinsLog.setRuleCode(getCoinsRule().getCode());
		coinsLogService.save(coinsLog);

		update(coinsLog);

		return coinsLog;
	}
}
