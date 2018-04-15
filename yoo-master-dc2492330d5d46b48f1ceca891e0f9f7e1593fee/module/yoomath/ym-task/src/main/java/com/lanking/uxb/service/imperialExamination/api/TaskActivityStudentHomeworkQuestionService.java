package com.lanking.uxb.service.imperialExamination.api;

import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;

/**
 * 提供学生作业习题相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月22日
 */
public interface TaskActivityStudentHomeworkQuestionService {
	/**
	 * 创建一个学生作业习题
	 * 
	 * @param studentHomeworkId
	 *            学生作业ID
	 * @param questionId
	 *            习题ID
	 * 
	 * @param sub
	 *            是否是子题
	 * @param correct
	 *            是否是订正题
	 * @param type
	 *            题目类型
	 * @return 学生作业习题
	 */
	StudentHomeworkQuestion create(long studentHomeworkId, long questionId, boolean sub, boolean correct, Type type);

}
