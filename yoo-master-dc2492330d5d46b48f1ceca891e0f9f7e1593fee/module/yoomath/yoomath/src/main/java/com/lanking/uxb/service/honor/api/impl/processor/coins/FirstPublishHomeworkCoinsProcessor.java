package com.lanking.uxb.service.honor.api.impl.processor.coins;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;

/**
 * 首次布置作业 100 金币
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月14日 下午7:11:46
 */
@Component
public class FirstPublishHomeworkCoinsProcessor extends AbstractCoinsProcessor {

	@Override
	public CoinsAction getAction() {
		return CoinsAction.FIRST_PUBLISH_HOMEWORK;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		return processOneTime(coinsLog);
	}
}
