package com.lanking.uxb.service.ranking.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassStat;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

public interface StaticDoQuestionSchoolStatService {

	CursorPage<Long, DoQuestionClassStat> queryDoQuestionClassStat(CursorPageable<Long> pageable);

	void staticDoQuestionClassStat(List<DoQuestionClassStat> classStats);

	void refreshDoQuestionSchoolStat();
}
