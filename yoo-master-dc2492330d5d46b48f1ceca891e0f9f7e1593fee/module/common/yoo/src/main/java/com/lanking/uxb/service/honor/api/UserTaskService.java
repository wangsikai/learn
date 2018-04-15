package com.lanking.uxb.service.honor.api;

import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskType;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskUserScope;
import com.lanking.uxb.service.honor.form.UserTaskQueryForm;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since 4.0.0
 */
public interface UserTaskService {
	/**
	 * 根据code获得用户任务数据
	 *
	 * @param code
	 *            任务code
	 * @return {@link UserTask}
	 */
	UserTask get(int code);

	/**
	 * 根据code列表获得Map结构数据code -> UserTask对象
	 *
	 * @param codes
	 *            用户任务code列表
	 * @return {@link Map}
	 */
	Map<Integer, UserTask> mget(Collection<Integer> codes);

	/**
	 * 根据code列表获得List结构数据
	 *
	 * @param codes
	 *            用户任务code列表
	 * @return {@link List}
	 */
	List<UserTask> mgetList(Collection<Integer> codes);

	/**
	 * 根据需求查找用户任务
	 *
	 * @param form
	 *            {@link UserTaskQueryForm}
	 * @return {@link List}
	 */
	List<UserTask> find(UserTaskQueryForm form);

	/**
	 * 查询用户未完成的任务列表
	 *
	 * @param form
	 *            {@link UserTaskQueryForm}
	 * @param userId
	 *            用户id
	 * @return {@link List}
	 */
	List<UserTask> findNotFindFinishUserTask(UserTaskQueryForm form, long userId);

}
