package com.lanking.uxb.service.honor.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsRule;

/**
 * 金币值规则接口
 * 
 * @since yoomath V1.8
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月9日
 */
public interface CoinsRuleService {

	/**
	 * 获取金币规则
	 * 
	 * @since 2.8
	 * @param action
	 *            {@link CoinsRule}
	 * @return {@link CoinsRule}
	 */
	CoinsRule getByAction(CoinsAction action);

	/**
	 * 获取金币规则
	 * 
	 * @since 2.8
	 * @param code
	 *            成长值规则的code
	 * @return {@link CoinsRule}
	 */
	CoinsRule getByCode(int code);

	/**
	 * 根据CoinsRule code 批量获得CoinsRule对象
	 *
	 * @since 2.8
	 * @param codes
	 *            CoinsRule code集合
	 * @return Map
	 */
	Map<Integer, CoinsRule> mgetCoinsRule(Collection<Integer> codes);

}
