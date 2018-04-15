package com.lanking.uxb.service.ranking.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolRankPraise;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.ranking.api.DoQuestionSchoolRankPraiseService;

import httl.util.CollectionUtils;

@Transactional(readOnly = true)
@Service
public class DoQuestionSchoolRankPraiseServiceImpl implements DoQuestionSchoolRankPraiseService {

	@Autowired
	@Qualifier("DoQuestionSchoolRankPraiseRepo")
	private Repo<DoQuestionSchoolRankPraise, Long> repo;

	@Override
	public DoQuestionSchoolRankPraise get(long id) {
		return repo.get(id);
	}

	@Override
	public Map<Long, DoQuestionSchoolRankPraise> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}

	@Override
	public Map<Long, DoQuestionSchoolRankPraise> getUserPraise(long userId) {
		Map<Long, DoQuestionSchoolRankPraise> data = new HashMap<>();
		List<DoQuestionSchoolRankPraise> praises = repo.find("$getUserSchoolPraise", Params.param("userId", userId))
				.list();
		if (CollectionUtils.isEmpty(praises)) {
			return data;
		}

		for (DoQuestionSchoolRankPraise v : praises) {
			data.put(v.getRankId(), v);
		}
		return data;
	}

	@Override
	public long countByRankId(long rankId, long userId) {
		Params param = Params.param();
		param.put("rankId", rankId);
		param.put("userId", userId);

		return repo.find("$countByRankId", param).count();
	}

	@Override
	public DoQuestionSchoolRankPraise getRankPraiseByRankId(long rankId, long userId) {
		Params param = Params.param();
		param.put("rankId", rankId);
		param.put("userId", userId);

		return repo.find("$getRankPraiseByRankId", param).get();
	}

	@Override
	public CursorPage<Long, DoQuestionSchoolRankPraise> getRankPraiseByRankId(long rankId,
			CursorPageable<Long> pageable) {
		return repo.find("$querySchoolRankPraiseByCursor", Params.param("rankId", rankId)).fetch(pageable);
	}

}
