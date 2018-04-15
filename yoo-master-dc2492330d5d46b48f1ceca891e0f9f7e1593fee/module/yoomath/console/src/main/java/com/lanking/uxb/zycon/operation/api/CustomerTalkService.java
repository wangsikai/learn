package com.lanking.uxb.zycon.operation.api;

import java.util.Map;

import com.lanking.cloud.sdk.data.Page;

public interface CustomerTalkService {
	/**
	 * 查询最新会话
	 * 
	 * @param tq
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Page<Map> querySession(TalkQuery tq);

	/**
	 * 查询与当前用户历史会话
	 * 
	 * @param tq
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Page<Map> queryLog(TalkQuery tq);

	/**
	 * 先查询个数，返回页面默认选中最后一页
	 * 
	 * @param tq
	 * @return
	 */
	Long getLogCount(TalkQuery tq);

	/**
	 * 通过用户id更新状态
	 * 
	 * @param userId
	 */
	void updateSessionByUserId(Long userId);

	/**
	 * 查询用户信息
	 * 
	 * @param userId
	 * @return
	 */
	Map<String, Object> queryUserInfo(Long userId);

}
