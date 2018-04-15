package com.lanking.uxb.service.honor.api.impl.processor.growth;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractGrowthProcessor;

/**
 * 首次布置作业 100成长值 一次性奖励
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月14日 下午7:08:12
 */
@Component
public class FirstPublishHomeworkGrowthProcessor extends AbstractGrowthProcessor {

	@Override
	public GrowthAction getAction() {
		return GrowthAction.FIRST_PUBLISH_HOMEWORK;
	}

	@Transactional
	@Override
	public GrowthLog process(GrowthLog growthLog, boolean isUpgrade) {
		return processOneTime(growthLog, isUpgrade);
	}
}
