package com.lanking.uxb.service.honor.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.honor.point.PointAction;
import com.lanking.cloud.domain.yoo.honor.point.PointRule;

/**
 * 积分规则相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年6月1日
 */
public interface PointRuleService {

	/**
	 * 获取积分规则
	 * 
	 * @since 2.1
	 * @param action
	 *            {@link PointAction}
	 * @return {@link PointRule}
	 */
	PointRule getByAction(PointAction action);

	/**
	 * 获取积分规则
	 * 
	 * @since 2.1
	 * @param code
	 *            积分规则的code
	 * @return {@link PointRule}
	 */
	PointRule getByCode(int code);

	/**
	 * 根据PointRule code 批量获得PointRule对象
	 *
	 * @since 2.1
	 * @param codes
	 *            PointRule code集合
	 * @return Map
	 */
	Map<Integer, PointRule> mgetPointRule(Collection<Integer> codes);

}
