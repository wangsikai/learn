package com.lanking.uxb.service.user.api;

import java.util.Map;

import com.lanking.cloud.domain.yoo.user.UserAction;

/**
 * 用户统一的动作行为调用接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月22日
 */
public interface UserActionService {

	/**
	 * 同步动作调用.
	 * 
	 * @param action
	 *            动作类型
	 * @param userId
	 *            用户ID
	 * @param params
	 *            附加参数
	 */
	void action(UserAction action, long userId, Map<String, Object> params);

	/**
	 * 异步动作调用.
	 * 
	 * @param action
	 *            动作类型
	 * @param userId
	 *            用户ID
	 * @param params
	 *            附加参数
	 */
	void asyncAction(UserAction action, long userId, Map<String, Object> params);
}
