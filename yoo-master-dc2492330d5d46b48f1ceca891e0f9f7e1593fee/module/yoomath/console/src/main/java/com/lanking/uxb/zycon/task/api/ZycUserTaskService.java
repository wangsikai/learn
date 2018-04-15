/**
 * 
 */
package com.lanking.uxb.zycon.task.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStatus;
import com.lanking.uxb.zycon.task.form.ZycUserTaskForm;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 *
 */
public interface ZycUserTaskService {
	UserTask get(int code);

	List<UserTask> list(Integer type);

	/**
	 * @param form
	 */
	void update(ZycUserTaskForm form);

	/**
	 * @param code1
	 * @param code2
	 */
	void doMove(int code1, int code2);

	/**
	 * @param form
	 */
	void create(ZycUserTaskForm form);

	/**
	 * 开启任务模块
	 *
	 * @param code
	 *            任务code
	 */
	void open(int code);

	/**
	 * 关闭任务模块
	 *
	 * @param code
	 *            任务code
	 */
	void close(int code);

	/**
	 * 更新任务状态
	 *
	 * @param code
	 *            任务code
	 */
	void completeInitData(int code);
}
