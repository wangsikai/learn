package com.lanking.uxb.zycon.task.api;

import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskType;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since 4.1.0
 */
public interface ZycUserTaskLogService {
	/**
	 * 关闭所有任务log
	 *
	 * @param taskCode
	 *            任务code
	 */
	void disabled(int taskCode);

	void disabled(Collection<Long> ids);

	CursorPage<Long, UserTaskLog> fetch(CursorPageable<Long> cursorPageable, int taskCode);

	/**
	 * 启用任务
	 *
	 * @param type
	 *            {@link UserTaskType}
	 */
	void enabled(UserTaskType type);

	/**
	 * mgetList根据用户id列表以及code
	 *
	 * @param userIds 用户列表
	 * @param code 任务code
	 * @return {@link List}
	 */
	List<UserTaskLog> mgetListByCodeAndUsers(Collection<Long> userIds, int code);

	/**
	 * mget根据用户id列表以及code 返回用户id -> UserTaskLog
	 *
	 * @param userIds
	 *            用户id列表
	 * @param code
	 *            任务code
	 * @return {@link Map}
	 */
	Map<Long, UserTaskLog> mgetByCodeAndUsers(Collection<Long> userIds, int code);

	/**
	 * 根据用户以及任务code查找日志数据
	 *
	 * @param taskCode
	 *            任务code
	 * @param userId
	 *            用户id
	 * @return {@link UserTaskLog}
	 */
	UserTaskLog findByUserTaskAndUser(int taskCode, long userId);
}
