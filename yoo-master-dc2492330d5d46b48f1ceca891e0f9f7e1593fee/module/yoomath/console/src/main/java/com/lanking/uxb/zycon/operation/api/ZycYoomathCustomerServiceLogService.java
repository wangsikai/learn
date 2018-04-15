package com.lanking.uxb.zycon.operation.api;

import com.lanking.cloud.domain.yoo.customerService.CustomerLogReadStatus;
import com.lanking.cloud.domain.yoo.customerService.YoomathCustomerServiceLog;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

import java.util.Collection;
import java.util.Map;

/**
 * 客服对话日志处理
 *
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
public interface ZycYoomathCustomerServiceLogService {
	/**
	 * 得到对话日志
	 *
	 * @param cursorPageable
	 *            {@link CursorPageable}
	 * @param userId
	 *            用户id
	 * @param status
	 *            未读状态
	 * @param csId
	 *            客服的id 信息读取状态
	 * @return {@link YoomathCustomerServiceLog}
	 */
	CursorPage<Long, YoomathCustomerServiceLog> pull(CursorPageable<Long> cursorPageable, long userId,
			CustomerLogReadStatus status, long csId);

	/**
	 * 得到当前未读数
	 *
	 * @return 未读信息数
	 */
	Long getUnreadCount();

	/**
	 * 根据用户得到用户
	 *
	 * @param userIds
	 *            用户id列表
	 * @return 用户id->发送的未读消息数量
	 */
	Map<Long, Long> mgetUserUnreadCount(Collection<Long> userIds);

	/**
	 * 得到某个用户的发送未读消息
	 *
	 * @param userId
	 *            用户id
	 * @return 用户发送消息未读数
	 */
	Long getUserUnreadCount(long userId);
}
