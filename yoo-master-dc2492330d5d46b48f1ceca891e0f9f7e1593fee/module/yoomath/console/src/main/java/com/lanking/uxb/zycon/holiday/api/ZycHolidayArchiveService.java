package com.lanking.uxb.zycon.holiday.api;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;

/**
 * 假期作业题目答案归档
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
public interface ZycHolidayArchiveService {
	/**
	 * 假期作业题目归档处理
	 *
	 * @param itemQuestionId
	 *            题目专项id
	 * @param result
	 *            人工批改结果
	 */
	void asyncAnswerArchive(long itemQuestionId, HomeworkAnswerResult result);
}
