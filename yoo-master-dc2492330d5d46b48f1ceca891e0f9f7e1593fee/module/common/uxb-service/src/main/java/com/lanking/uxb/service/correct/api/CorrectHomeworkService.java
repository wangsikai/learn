package com.lanking.uxb.service.correct.api;

import com.lanking.cloud.domain.yoomath.homework.Homework;

/**
 * 批改流程使用的作业接口.
 * 
 * @author wanlong.che
 * @since 小优快批
 */
public interface CorrectHomeworkService {

	/**
	 * 获取作业
	 * 
	 * @param id
	 *            作业ID
	 * @return {@link Homework}
	 */
	Homework get(long id);

	/**
	 * 设置待批改标记.
	 * 
	 * @param tobeCorrected
	 *            是否待批改
	 */
	void setTobeCorrected(long homeworkId, Boolean tobeCorrected);

	/**
	 * 添加批改完成的学生作业数量.
	 * 
	 * @param homeworkId
	 *            作业ID
	 */
	void addCorrectingCount(long homeworkId);

	/**
	 * 设置作业全部已批改完成.
	 * 
	 * @param homeworkId
	 *            作业ID
	 */
	void allCorrectComplete(long homeworkId);

	/**
	 * 判断作业是否全部批改完成.
	 * 
	 * @param homeworkId
	 *            作业ID
	 */
	void checkAndSaveHomeworkCorrectComplete(long homeworkId);
}
