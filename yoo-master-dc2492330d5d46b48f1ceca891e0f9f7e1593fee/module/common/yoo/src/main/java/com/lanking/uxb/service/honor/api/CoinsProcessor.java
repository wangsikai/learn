package com.lanking.uxb.service.honor.api;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsRule;

/**
 * 金币处理器接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月9日
 */
public interface CoinsProcessor {

	boolean accpet(CoinsAction action);

	CoinsAction getAction();

	CoinsRule getCoinsRule();

	CoinsLog process(CoinsLog coinsLog);

}
