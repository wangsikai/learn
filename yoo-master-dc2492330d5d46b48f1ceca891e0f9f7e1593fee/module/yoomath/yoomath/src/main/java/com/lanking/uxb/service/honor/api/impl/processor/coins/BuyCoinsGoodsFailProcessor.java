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
 * 金币商城购买商品金币处理
 *
 * 如果为负则表示购买商品扣除费用 如果为正说明兑换失败需要返还相应的金额
 *
 * @author xinyu.zhou
 * @since 2.0.3
 */
@Component
public class BuyCoinsGoodsFailProcessor extends AbstractCoinsProcessor {
	@Autowired
	private CoinsLogService coinsLogService;

	@Override
	public CoinsAction getAction() {
		return CoinsAction.BUY_COINS_GOODS_FAIL;
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
