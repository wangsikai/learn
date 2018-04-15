package com.lanking.uxb.service.honor.api;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;

import java.util.Map;

/**
 * UserTaskProcessor处理方法定义接口
 *
 * @author xinyu.zhou
 * @since 4.1.0
 */
public interface UserTaskProcessor {
	/**
	 * 是否接收处理
	 *
	 * @param code
	 *            任务码
	 * @return 是否接收
	 */
	boolean accept(int code);

	/**
	 * 获得任务码
	 *
	 * @return 任务码
	 */
	int getCode();

	/**
	 * 处理任务相关数据
	 *
	 * @param userTask
	 *            用户任务
	 * @param userId
	 *            用户id
	 * @param params
	 *            处理参数
	 */
	void process(UserTask userTask, long userId, Map<String, Object> params);

	UserTask getUserTask(int code);

	UserTask getUserTask(int code, long userId, Map<String, Object> params);
}
