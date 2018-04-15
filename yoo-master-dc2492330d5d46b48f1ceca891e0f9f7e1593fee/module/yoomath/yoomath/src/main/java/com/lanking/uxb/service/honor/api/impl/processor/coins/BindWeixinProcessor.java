package com.lanking.uxb.service.honor.api.impl.processor.coins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;

/**
 * 绑定WEIXIN加金币
 * 
 * @since 2.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年8月16日
 */
@Component
public class BindWeixinProcessor extends AbstractCoinsProcessor {
	@Autowired
	private CoinsLogService coinsLogService;

	@Override
	public CoinsAction getAction() {
		return CoinsAction.BIND_WEIXIN;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		return processOneTime(coinsLog);
	}

}
