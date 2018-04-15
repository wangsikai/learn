package com.lanking.uxb.service.activity.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ExerciseQuestion;

/**
 * 假期活动01练习题目接口
 * 
 * @author zemin.song
 *
 */
public interface HolidayActivity01ExerciseQuestionService {

	/**
	 * 练习查询作业
	 * 
	 * @param exerciseIds
	 *            练习id
	 */
	List<HolidayActivity01ExerciseQuestion> queryQuestionList(long activityCode, long exerciseId);

	/**
	 * 批量练习查询作业
	 * 
	 * @param exerciseIds
	 *            练习id
	 */
	List<HolidayActivity01ExerciseQuestion> queryQuestionList(long activityCode, Collection<Long> exerciseIds);

	/**
	 * 批量练习查询作业
	 * 
	 * @param exerciseIds
	 *            练习id
	 */
	Map<Long, List<HolidayActivity01ExerciseQuestion>> queryQuestionMap(long activityCode,
			Collection<Long> exerciseIds);

}
