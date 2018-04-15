package com.lanking.uxb.service.activity.api;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Exercise;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ExerciseType;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Grade;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 假期活动01练习接口
 * 
 * @author zemin.song
 *
 */
public interface HolidayActivity01ExerciseService {

	/**
	 * 游标查询活动预置作业
	 *
	 * @param categoryCode
	 *            教材版本
	 * @param userId
	 *            用户id
	 * @param type
	 *            习题类型
	 * @param grade
	 *            年级
	 * @param cursorPageable
	 *            游标分页参数
	 * @return {@link CursorPage}
	 * 
	 */
	CursorPage<Integer, HolidayActivity01Exercise> list(long activityCode, Integer categoryCode, Long userId,
			HolidayActivity01ExerciseType type, HolidayActivity01Grade grade, CursorPageable<Integer> cursorPageable);

}
