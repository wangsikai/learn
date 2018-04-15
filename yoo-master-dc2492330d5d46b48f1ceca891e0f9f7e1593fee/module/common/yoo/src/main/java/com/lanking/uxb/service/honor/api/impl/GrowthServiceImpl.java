package com.lanking.uxb.service.honor.api.impl;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.uxb.service.honor.api.GrowthProcessor;
import com.lanking.uxb.service.honor.api.GrowthService;

@Transactional(readOnly = true)
@Service
public class GrowthServiceImpl implements GrowthService, ApplicationContextAware {
	private boolean init = false;
	private ApplicationContext appContext;
	private List<GrowthProcessor> processors = Lists.newArrayList();

	@Transactional
	@Override
	public GrowthLog grow(GrowthAction action, long userId, int growthValue, Biz biz, long bizId, boolean isUpgrade) {
		GrowthProcessor processor = getProcessor(action);
		return processor.process(new GrowthLog(userId, growthValue, biz, bizId), isUpgrade);
	}

	@Transactional
	@Override
	public GrowthLog grow(GrowthAction action, long userId, boolean isUpgrade) {
		GrowthProcessor processor = getProcessor(action);
		return processor.process(new GrowthLog(userId), isUpgrade);
	}

	public void initProcessors() {
		if (!init) {
			for (GrowthProcessor processor : appContext.getBeansOfType(GrowthProcessor.class).values()) {
				processors.add(processor);
			}
			init = true;
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}

	GrowthProcessor getProcessor(GrowthAction action) {
		initProcessors();
		GrowthProcessor processor = null;
		for (GrowthProcessor p : processors) {
			if (p.accpet(action)) {
				processor = p;
			}
		}
		return processor;
	}

}
