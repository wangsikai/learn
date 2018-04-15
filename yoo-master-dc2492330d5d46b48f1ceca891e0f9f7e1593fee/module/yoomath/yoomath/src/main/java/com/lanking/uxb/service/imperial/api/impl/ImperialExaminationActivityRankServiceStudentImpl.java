package com.lanking.uxb.service.imperial.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRankStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityRankServiceStudent;

@Service
@Transactional(readOnly = true)
public class ImperialExaminationActivityRankServiceStudentImpl
		implements ImperialExaminationActivityRankServiceStudent {

	@Autowired
	@Qualifier("ImperialExaminationActivityRankStudentRepo")
	private Repo<ImperialExaminationActivityRankStudent, Long> repo;

	@Override
	public ImperialExaminationActivityRankStudent queryBest(long code, ImperialExaminationType type, long userId) {
		Params params = Params.param("code", code);
		params.put("type", type.getValue());
		params.put("userId", userId);
		return repo.find("$queryBest", params).get();
	}

	@Override
	public List<Map> queryTopList(long code, ImperialExaminationType type, int limit) {
		Params params = Params.param("code", code);
		params.put("type", type.getValue());
		params.put("limit", limit);

		return repo.find("$queryTopList", params).list(Map.class);
	}

	@Override
	public Long getRank(long code, ImperialExaminationType type, Integer score, Integer doTime, Date submitAt) {
		Params params = Params.param("code", code);
		params.put("type", type.getValue());
		params.put("score", score);
		params.put("doTime", doTime);
		if (submitAt != null) {
			params.put("submitAt", submitAt);
		}

		Long count = repo.find("$getRank", params).get(Long.class);
		return count + 1;
	}

}
