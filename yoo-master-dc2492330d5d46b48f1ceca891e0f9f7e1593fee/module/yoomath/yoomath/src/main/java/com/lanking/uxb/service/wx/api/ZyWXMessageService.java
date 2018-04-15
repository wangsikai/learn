package com.lanking.uxb.service.wx.api;

import java.util.Collection;

/**
 * 作业相关的微信消息.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2016年1月4日
 */
public interface ZyWXMessageService {

	/**
	 * 作业发布消息.
	 * 
	 * @param homeworkClassIds
	 *            作业班级
	 * @param homeworkId
	 *            作业ID
	 */
	void sendPublishHomeworkMessage(Collection<Long> homeworkClassIds, long homeworkId, Collection<Long> groupIds);

	/**
	 * 下发作业消息.
	 * 
	 * @param homeworkId
	 *            作业ID
	 */
	void sendIssuedHomeworkMessage(long homeworkId);
}
