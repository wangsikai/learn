package com.lanking.uxb.service.holidayActivity01.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ExerciseQuestion;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Homework;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 假期作业
 * 
 * @author zemin.song
 *
 */
public interface TaskHolidayActivity01HomeWorkService {
	/**
	 * 开始布置
	 * 
	 * @return
	 */
	void publish(List<HolidayActivity01Homework> activityhks);

	/**
	 * 分页获取假期活动作业(homework_id为空)
	 * 
	 * @param activityCode
	 *            活动code
	 * @param cursorPageable
	 * @return
	 */
	CursorPage<Long, HolidayActivity01Homework> findHolidayActivity01HkList(Long activityCode,
			CursorPageable<Long> cursorPageable);

	/**
	 * 批量获取活动练习题目
	 * 
	 * @param activityCode
	 *            活动code
	 * @param exerciseIds
	 *            练习Ids
	 * @return
	 */
	Map<Long, List<HolidayActivity01ExerciseQuestion>> queryQuestionMap(Collection<Long> exerciseIds);

	/**
	 * 更新homeworkId
	 * 
	 * @param id
	 * @return
	 */
	void update(Long id, Long homeworkId);

}
