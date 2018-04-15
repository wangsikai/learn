package com.lanking.uxb.zycon.operation.api;

import com.lanking.cloud.domain.yoo.customerService.YoomathCustomerServiceSession;
import com.lanking.cloud.sdk.bean.Status;

import java.util.List;

/**
 * 会话信息service
 *
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
public interface ZycYoomathCustomerServiceSessionService {

	/**
	 * 得到所有会话信息
	 *
	 * @return {@link YoomathCustomerServiceSession}
	 */
	List<YoomathCustomerServiceSession> findAll();

	/**
	 * 根据用户的id得到对话session
	 *
	 * @param userId
	 *            用户的id
	 * @return {@link YoomathCustomerServiceSession}
	 */
	YoomathCustomerServiceSession getByUser(long userId);

	/**
	 * 更新对话信息
	 *
	 * @param userId
	 *            用户id
	 * @param status
	 *            更新状态 对话信息
	 * @return {@link YoomathCustomerServiceSession}
	 */
	YoomathCustomerServiceSession update(long userId, Status status);

}
