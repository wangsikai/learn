package com.lanking.uxb.service.activity.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Exercise;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ExerciseType;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Grade;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.HolidayActivity01ExerciseService;

/**
 * 假期活动01练习接口实现
 * 
 * @author zemin.song
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity01ExerciseServiceImpl implements HolidayActivity01ExerciseService {

	@Autowired
	@Qualifier("HolidayActivity01ExerciseRepo")
	private Repo<HolidayActivity01Exercise, Long> repo;

	@Override
	public CursorPage<Integer, HolidayActivity01Exercise> list(long activityCode, Integer categoryCode, Long userId,
			HolidayActivity01ExerciseType type, HolidayActivity01Grade grade, CursorPageable<Integer> cursorPageable) {
		Params params = Params.param("activityCode", activityCode);
		if (categoryCode != null) {
			params.put("categoryCode", categoryCode);
		}
		if (userId != null) {
			params.put("userId", userId);
		}
		if (type != null) {
			params.put("type", type.getValue());
		}
		if (grade != null) {
			params.put("grade", grade.getValue());
		}
		return repo.find("$listHolidayActivity01Exercise", params).fetch(cursorPageable,
				HolidayActivity01Exercise.class, new CursorGetter<Integer, HolidayActivity01Exercise>() {
					public Integer getCursor(HolidayActivity01Exercise bean) {
						return bean.getSequence();
					}
				});
	}

}
