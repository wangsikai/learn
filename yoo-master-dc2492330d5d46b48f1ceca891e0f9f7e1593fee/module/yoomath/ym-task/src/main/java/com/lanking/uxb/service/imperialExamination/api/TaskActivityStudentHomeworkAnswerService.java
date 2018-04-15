package com.lanking.uxb.service.imperialExamination.api;

import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;

/**
 * 提供学生作业答案相关接口
 * 
 * @author zemin.song
 * @version 2017年4月12日
 */
public interface TaskActivityStudentHomeworkAnswerService {
	/**
	 * 创建学生作业答案
	 * 
	 * @param studentHomeworkQuestionId
	 *            学生作业习题ID
	 * @param sequence
	 *            序号
	 * @return 学生作业答案
	 */
	StudentHomeworkAnswer create(long studentHomeworkQuestionId, int sequence);
}
