package com.lanking.uxb.service.honor.api.impl;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.uxb.service.honor.api.CoinsProcessor;
import com.lanking.uxb.service.honor.api.CoinsService;

@Transactional(readOnly = true)
@Service
public class CoinsServiceImpl implements CoinsService, ApplicationContextAware, InitializingBean {

	private ApplicationContext appContext;
	private List<CoinsProcessor> processors = Lists.newArrayList();

	@Transactional
	@Override
	public CoinsLog earn(CoinsAction action, Long userId, int coinsValue, Biz biz, long bizId) {
		CoinsProcessor processor = getProcessor(action);
		return processor.process(new CoinsLog(userId, coinsValue, biz, bizId));
	}

	@Transactional
	@Override
	public CoinsLog earn(CoinsAction action, Long userId) {
		CoinsProcessor processor = getProcessor(action);
		return processor.process(new CoinsLog(userId));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}

	CoinsProcessor getProcessor(CoinsAction action) {
		CoinsProcessor processor = null;
		for (CoinsProcessor p : processors) {
			if (p.accpet(action)) {
				processor = p;
			}
		}
		return processor;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (CoinsProcessor processor : appContext.getBeansOfType(CoinsProcessor.class).values()) {
			processors.add(processor);
		}
	}
}
