package com.lanking.uxb.service.honor.api;

import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthRule;

/**
 * 成长值处理器接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月9日
 */
public interface GrowthProcessor {

	boolean accpet(GrowthAction action);

	GrowthAction getAction();

	GrowthRule getGrowthRule();

	GrowthLog process(GrowthLog growthLog, boolean isUpgrade);

}
