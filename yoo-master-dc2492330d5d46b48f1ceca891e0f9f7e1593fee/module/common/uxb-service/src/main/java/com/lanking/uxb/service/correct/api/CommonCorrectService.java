package com.lanking.uxb.service.correct.api;

import java.util.Collection;

import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectLog;

/**
 * 批改相关通用接口.
 * 
 * @author wanlong.che
 * @since 小优快批 2018-2-8
 *
 */
public interface CommonCorrectService {

	/**
	 * 创建订正题（异步）.
	 * 
	 * @param studentHomeworkQuestionId
	 *            学生作业习题ID
	 */
	void asyncCreateStudentHomeworkCorrectQuestion(long studentHomeworkQuestionId);

	/**
	 * 创建订正题（异步）.
	 * 
	 * @param studentHomeworkQuestionIds
	 *            学生作业习题ID集合
	 */
	void asyncCreateStudentHomeworkCorrectQuestion(Collection<Long> studentHomeworkQuestionIds);

	/**
	 * 保存批改Log.
	 * 
	 * @param log
	 */
	void asyncCreateCorrectLog(QuestionCorrectLog log);

	/**
	 * 保存批改Log.
	 * 
	 * @param log
	 */
	void asyncCreateCorrectLog(Collection<QuestionCorrectLog> logs);
}
