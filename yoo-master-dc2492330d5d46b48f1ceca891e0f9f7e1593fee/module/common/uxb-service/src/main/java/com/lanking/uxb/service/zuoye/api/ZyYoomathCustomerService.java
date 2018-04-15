package com.lanking.uxb.service.zuoye.api;

import com.lanking.cloud.domain.yoo.customerService.CustomerLogReadStatus;
import com.lanking.cloud.domain.yoo.customerService.YoomathCustomerServiceLog;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 客服对话日志服务
 *
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
public interface ZyYoomathCustomerService {

	// 默认客服ID
	long DEF_CUSTOMER_ID = 1L;

	/**
	 * 查找最新一条的对话日志
	 *
	 * @param userId
	 *            用户id
	 * @return {@link YoomathCustomerServiceLog}
	 */
	YoomathCustomerServiceLog pullNewestOne(long userId);

	/**
	 * 得到对话日志
	 *
	 * @param cursorPageable
	 *            {@link CursorPageable}
	 * @param userId
	 *            用户id
	 * @param status
	 *            信息读取状态
	 * @return {@link YoomathCustomerServiceLog}
	 */
	CursorPage<Long, YoomathCustomerServiceLog> pull(CursorPageable<Long> cursorPageable, long userId,
			CustomerLogReadStatus status);

	/**
	 * 发送消息
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @param userId
	 *            用户ID
	 * @param customerId
	 *            客服ID
	 * @return 客服会话
	 */
	void send(long userId, long customerId, String content);

}
