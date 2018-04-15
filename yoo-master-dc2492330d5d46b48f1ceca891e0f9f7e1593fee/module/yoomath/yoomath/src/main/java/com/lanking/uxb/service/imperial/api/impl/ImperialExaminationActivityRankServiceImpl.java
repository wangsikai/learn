package com.lanking.uxb.service.imperial.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRank;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityRankService;

/**
 * 科举活动--科举活动排名表
 * 
 * @author wangsenhao
 * @version 2017年4月6日
 */
@Service
@Transactional(readOnly = true)
public class ImperialExaminationActivityRankServiceImpl implements ImperialExaminationActivityRankService {
	@Autowired
	@Qualifier("ImperialExaminationActivityRankRepo")
	private Repo<ImperialExaminationActivityRank, Long> repo;

	@Override
	public ImperialExaminationActivityRank queryBest(long code, ImperialExaminationType type, long userId) {
		Params params = Params.param("code", code);
		params.put("type", type.getValue());
		params.put("userId", userId);
		return repo.find("$queryBest", params).get();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> queryTopList(long code, ImperialExaminationType type, int limit) {
		Params params = Params.param("code", code);
		params.put("type", type.getValue());
		params.put("limit", limit);
		return repo.find("$queryTopList", params).list(Map.class);
	}

	@Override
	public Long getRank(long code, ImperialExaminationType type, Integer score, Integer doTime) {
		Params params = Params.param("code", code);
		params.put("type", type.getValue());
		params.put("score", score);
		params.put("doTime", doTime);
		Long count = repo.find("$getRank", params).get(Long.class);
		return count + 1;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> queryTopList2(long code, ImperialExaminationType type, int limit, int room) {
		Params params = Params.param("code", code);
		params.put("type", type.getValue());
		params.put("limit", limit);
		params.put("room", room);

		return repo.find("$queryTopList2", params).list(Map.class);
	}

	@Override
	public Long getRank2(long code, ImperialExaminationType type, Integer score, Integer doTime, Integer room) {
		Params params = Params.param("code", code);
		params.put("type", type.getValue());
		params.put("score", score);
		params.put("doTime", doTime);
		params.put("room", room);
		Long count = repo.find("$getRank2", params).get(Long.class);
		return count + 1;
	}
}
