package com.lanking.uxb.service.ranking.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassStat;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

public interface StaticDoQuestionClassStatService {

	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> queryClassId(CursorPageable<Long> pageable);

	Map<Long, DoQuestionClassStat> listOneClass(long classId);

	void staticDoQuestionClassStat(int day, List<Long> classIds, List<Long> teacherIds);

	void refreshDoQuestionClassStat();

}
