package com.lanking.uxb.service.honor.api.impl.processor.coins;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;

/**
 * 初次创建班级
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月9日 下午2:49:28
 */
@Component
public class CreateClazzCoinsProcessor extends AbstractCoinsProcessor {

	@Override
	public CoinsAction getAction() {
		return CoinsAction.CREATE_CLAZZ;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog growthLog) {
		return processOneTime(growthLog);
	}
}
