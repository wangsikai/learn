package com.lanking.uxb.service.user.api;

import java.util.Map;

import com.lanking.cloud.domain.yoo.user.UserAction;

public interface UserActionProcessor {

	/**
	 * 是否开启处理器.
	 * 
	 * @param action
	 *            用户动作
	 * @return
	 */
	boolean on(UserAction action);

	/**
	 * 是否匹配处理器.
	 * 
	 * @param action
	 *            用户动作
	 * @return
	 */
	boolean accpet(UserAction action);

	/**
	 * 运行处理器.
	 * 
	 * @param action
	 *            用户动作
	 * @param params
	 *            附加参数
	 */
	void process(UserAction action, long userId, Map<String, Object> params);
}
