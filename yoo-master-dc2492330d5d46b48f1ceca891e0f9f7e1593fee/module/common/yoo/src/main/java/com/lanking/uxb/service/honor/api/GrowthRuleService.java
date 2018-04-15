package com.lanking.uxb.service.honor.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthRule;

/**
 * 成长值规则相关接口
 * 
 * @since yoomath V1.8
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月9日
 */
public interface GrowthRuleService {
	/**
	 * 获取成长值规则
	 * 
	 * @since 2.8
	 * @param action
	 *            {@link GrowthAction}
	 * @return {@link GrowthRule}
	 */
	GrowthRule getByAction(GrowthAction action);

	/**
	 * 获取成长值规则
	 * 
	 * @since 2.8
	 * @param code
	 *            成长值规则的code
	 * @return {@link GrowthRule}
	 */
	GrowthRule getByCode(int code);

	/**
	 * 根据GrowthRule code 批量获得GrowthRule对象
	 *
	 * @since 2.8
	 * @param codes
	 *            GrowthRule code集合
	 * @return Map
	 */
	Map<Integer, GrowthRule> mgetGrowthRule(Collection<Integer> codes);

}
