package com.lanking.uxb.service.imperialExamination.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.homework.Exercise;
import com.lanking.uxb.service.resources.ex.ExerciseException;

/**
 * 练习题相关接口
 * 
 * @since 2.1
 * @author zemin.song
 * @version 2017年4月11日
 */
public interface TaskActivityExerciseService {
	/**
	 * 获取一个练习
	 * 
	 * @param id
	 *            练习ID
	 * @return 练习对象
	 */
	Exercise get(long id);

	/**
	 * 创建一个空的练习
	 * 
	 * @since 2.1
	 * @param exercise
	 *            Exercise
	 * @return Exercise
	 */
	Exercise createBlankExercise(Exercise exercise) throws ExerciseException;

	/**
	 * 创建一个练习,包含习题
	 * 
	 * @since 2.1
	 * @param exercise
	 *            Exercise
	 * @param questionIds
	 *            习题IDs
	 * @return Exercise
	 */
	Exercise createExercise(Exercise exercise, List<Long> questionIds) throws ExerciseException;

}
