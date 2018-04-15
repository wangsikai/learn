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

/**
 * 成长值等级升级 金币奖励
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月10日 下午1:31:14
 */
@Component
public class GradeUpRewardCoinsProcessor extends AbstractCoinsProcessor {

	@Autowired
	private CoinsLogService coinsLogService;

	@Override
	public CoinsAction getAction() {
		return CoinsAction.GRADE_UP_REWARD;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		coinsLog.setCreateAt(new Date());
		coinsLog.setRuleCode(getCoinsRule().getCode());
		coinsLog.setCoinsValue(getCoinsRule().getCoinsValue());
		coinsLog.setStatus(Status.ENABLED);
		coinsLogService.save(coinsLog);
		updateNoSave(coinsLog);
		coinsLog.setUpRewardCoins(getCoinsRule().getCoinsValue());
		return coinsLog;
	}
}
