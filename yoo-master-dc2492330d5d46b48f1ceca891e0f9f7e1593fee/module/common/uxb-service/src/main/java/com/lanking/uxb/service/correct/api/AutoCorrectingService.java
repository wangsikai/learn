package com.lanking.uxb.service.correct.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;

/**
 * 自动批改接口.
 * 
 * @author wanlong.che
 * @since 小优快批
 */
public interface AutoCorrectingService {

	/**
	 * 自动批改作业
	 * 
	 * @param homework
	 *            作业
	 * @param studentHomework
	 *            学生作业
	 * @param studentHomeworkQuestions
	 *            学生作业习题集合
	 * 
	 * @return Map<Question.Type, Integer> 返回无法自动批改的习题个数
	 */
	Map<Question.Type, Integer> autoCorrecting(Homework homework,
			List<StudentHomeworkQuestion> studentHomeworkQuestions);

}
