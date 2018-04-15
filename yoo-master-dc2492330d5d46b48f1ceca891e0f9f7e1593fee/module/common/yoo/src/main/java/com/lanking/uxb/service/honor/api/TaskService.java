package com.lanking.uxb.service.honor.api;

import java.util.Map;

/**
 * @author xinyu.zhou
 * @since 4.1.0
 */
public interface TaskService {

	/**
	 * 处理用户任务
	 *
	 * @param taskCode
	 *            任务code
	 * @param userId
	 *            用户id
	 * @param params
	 *            处理的参数数据
	 */
	void process(int taskCode, long userId, Map<String, Object> params);
}
