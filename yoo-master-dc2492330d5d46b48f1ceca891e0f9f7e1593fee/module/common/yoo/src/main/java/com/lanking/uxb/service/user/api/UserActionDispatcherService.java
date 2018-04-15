package com.lanking.uxb.service.user.api;

import java.util.Map;

import com.lanking.cloud.domain.yoo.user.UserAction;

/**
 * 用户行为处理器调度.
 * 
 * @author wlche
 *
 */
public interface UserActionDispatcherService {

	/**
	 * 调度.
	 * 
	 * @param action
	 *            动作
	 * @param userId
	 *            动作用户
	 * @param params
	 *            附加数据
	 */
	void process(UserAction action, long userId, Map<String, Object> params);
}
