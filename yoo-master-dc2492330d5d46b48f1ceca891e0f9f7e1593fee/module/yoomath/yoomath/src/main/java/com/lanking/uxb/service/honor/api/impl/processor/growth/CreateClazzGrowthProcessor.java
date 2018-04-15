package com.lanking.uxb.service.honor.api.impl.processor.growth;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractGrowthProcessor;

/**
 * 初次创建班级
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月9日 下午2:49:28
 */
@Component
public class CreateClazzGrowthProcessor extends AbstractGrowthProcessor {

	@Override
	public GrowthAction getAction() {
		return GrowthAction.CREATE_CLAZZ;
	}

	@Transactional
	@Override
	public GrowthLog process(GrowthLog growthLog, boolean isUpgrade) {
		return processOneTime(growthLog, isUpgrade);
	}
}
