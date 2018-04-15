package com.lanking.uxb.zycon.homework.api;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;

public interface ZycAutoCorrectingService {
	/**
	 * 人工批改后调用(目前只支持填空题)
	 *
	 * @param stuHkId
	 *            学生作业ID
	 * @param stuHkQId
	 *            学生作业题ID
	 * @param result
	 *            批改结果
	 * @since yoomath V1.3
	 */
	void asyncAutoCheck(long stuHkId, long stuHkQId, HomeworkAnswerResult result);
}
