package com.lanking.uxb.ycoorect.service;

import com.lanking.cloud.domain.support.resources.question.QuestionError;

/**
 * 批改员错题反馈服务.
 * 
 * @author wanlong.che
 *
 */
public interface YooCorrectQuestionErrorService {

	/**
	 * 保存 QuestionError.
	 * 
	 * @param questionError
	 */
	void saveCorrectQuestionError(long studentHomeworkQuestionId, QuestionError questionError);

	/**
	 * 获取 QuestionError
	 * 
	 * @param studentHomeorkQuestionId
	 *            学生作业习题ID
	 * @return
	 */
	QuestionError get(long studentHomeworkQuestionId);
}
