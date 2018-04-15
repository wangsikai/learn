package com.lanking.uxb.service.resources.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.homework.ExerciseQuestion;
import com.lanking.uxb.service.resources.ex.ExerciseException;

/**
 * 提供练习题相关操作接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月16日
 */
public interface ExerciseQuestionService {

	/**
	 * 根据练习ID获取练习题集合
	 * 
	 * @param exerciseId
	 *            练习ID
	 * @return 练习题集合
	 * @throws ExerciseException
	 */
	List<ExerciseQuestion> getExerciseQuestion(long exerciseId) throws ExerciseException;

	/**
	 * 根据练习ID获取习题ID集合
	 * 
	 * @param exerciseId
	 * @return 习题ID集合
	 * @throws ExerciseException
	 */
	List<Long> getQuestion(long exerciseId) throws ExerciseException;

	/**
	 * 插入 一条习题
	 * 
	 * @param exerciseId
	 *            练习ID
	 * @param questionId
	 *            习题ID
	 * @param initSequence
	 *            指定练习序号(为空时自动追加在练习最后)
	 * @return 练习题
	 * @throws ExerciseException
	 */
	ExerciseQuestion appendQuestion(long exerciseId, long questionId, Integer initSequence) throws ExerciseException;

}
