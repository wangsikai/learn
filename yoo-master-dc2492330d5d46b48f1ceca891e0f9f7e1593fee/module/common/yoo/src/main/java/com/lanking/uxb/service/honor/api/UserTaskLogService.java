package com.lanking.uxb.service.honor.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskType;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;

/**
 * @author xinyu.zhou
 * @since 4.0.0
 */
public interface UserTaskLogService {
	/**
	 * 根据用户以及UserTask.code来查找任务数据
	 *
	 * @param code
	 *            UserTask.code
	 * @param userId
	 *            用户id
	 * @return {@link UserTaskLog}
	 */
	UserTaskLog findByCodeAndUser(int code, long userId);

	/**
	 * 根据用户id以及UserTask.code列表查找任务数据
	 *
	 * @param codes
	 *            任务code列表
	 * @param userId
	 *            用户id
	 * @return {@link List}
	 */
	List<UserTaskLog> findByCodeAndUser(Collection<Integer> codes, long userId);

	/**
	 * 查找用户的任务根据code以及日期
	 *
	 * @param code
	 *            任务code
	 * @param userId
	 *            用户id
	 * @param date
	 *            日期
	 * @return {@link UserTaskLog}
	 */
	UserTaskLog findByCodeAndUser(int code, long userId, Date date);

	/**
	 * 根据UserTask code列表查询用户任务数据
	 *
	 * @param codes
	 *            UserTask.code 列表
	 * @param userId
	 *            用户id
	 * @return {@link Map}
	 */
	Map<Integer, UserTaskLog> query(Collection<Integer> codes, long userId);

	/**
	 * 根据id获得数据
	 *
	 * @param id
	 *            用户UserTaskLog.id
	 * @return {@link UserTaskLog}
	 */
	UserTaskLog get(long id);

	/**
	 * mgetList UserTaskLog数据
	 *
	 * @param ids
	 *            UserTaskLog id列表
	 * @return UserTaskLog列表
	 */
	List<UserTaskLog> mgetList(Collection<Long> ids);

	/**
	 * mget UserTaskLog数据
	 *
	 * @param ids
	 *            UserTaskLog id列表
	 * @return UserTaskLog Map数据 id->UserTaskLog
	 */
	Map<Long, UserTaskLog> mget(Collection<Long> ids);

	/**
	 * 更新或者创建用户任务日志
	 *
	 * @param form
	 *            {@link UserTaskLogUpdateForm}
	 * @return {@link UserTaskLog}
	 */
	UserTaskLog update(UserTaskLogUpdateForm form);

	/**
	 * 领取任务相关奖励
	 *
	 * @param id
	 *            任务记录id
	 * @return 领取任务的返回相关数据
	 */
	Map<String, Object> earn(long id);

	/**
	 * 统计任务数
	 * 
	 * @param code
	 *            任务code
	 * @param userId
	 *            用户id
	 * @return 统计数量
	 */
	long count(long code, long userId);

	/**
	 * 根据任务类型统计未领取的数量
	 *
	 * @param userTaskType
	 *            {@link UserTaskType}
	 * @param userId
	 *            用户id
	 * @return 数量
	 */
	Long countNotReceiveTask(UserTaskType userTaskType, long userId);

	/**
	 * 取最新一条完成的任务时间（按类型）
	 *
	 * @param type
	 *            {@link UserTaskType}
	 * @param userId
	 *            用户id
	 * @return 最后一条时间
	 */
	Date getLatestCompleteDate(UserTaskType type, long userId);

	/**
	 * 获得奖励
	 *
	 * @param userTask
	 *            {@link UserTask}
	 * @param userId
	 *            用户id
	 */
	void receiveReward(UserTask userTask, long userId);
}
