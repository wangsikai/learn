package com.lanking.uxb.service.ranking.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

public interface StaticDoQuestionStudentStatService {

	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> queryUserId(CursorPageable<Long> pageable);

	void staticDoQuestionStudentStat(int day, List<Long> userIds);

	void refreshDoQuestionStudentStat();
}
