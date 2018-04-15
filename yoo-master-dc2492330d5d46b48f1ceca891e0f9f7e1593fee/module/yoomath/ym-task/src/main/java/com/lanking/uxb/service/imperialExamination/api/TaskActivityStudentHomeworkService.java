package com.lanking.uxb.service.imperialExamination.api;

import java.util.Date;
import java.util.Set;

import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.uxb.service.resources.ex.HomeworkException;

/**
 * 提供学生作业相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月14日
 */
public interface TaskActivityStudentHomeworkService {

	/**
	 * 发布作业
	 * 
	 * @param homework
	 *            作业
	 * @param studentIds
	 *            学生IDs
	 * @param createAt
	 *            创建时间
	 * @throws HomeworkException
	 */
	void publishHomework(Homework homework, Set<Long> studentIds, Date createAt) throws HomeworkException;
}
