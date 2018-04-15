package com.lanking.cloud.job.nationalDayActivity.service;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01;

public interface NationalDayActivity01TeaService {

	/**
	 * 查询指定数量的teacherId,做去重处理
	 * 
	 * @param startindex
	 * @param size
	 * @return
	 */
	List<Long> getTeacherIdsSpecify(int startindex, int size);

	void saveTea(List<Long> teacherIds, NationalDayActivity01 activity);

	NationalDayActivity01 getActivity();
}
