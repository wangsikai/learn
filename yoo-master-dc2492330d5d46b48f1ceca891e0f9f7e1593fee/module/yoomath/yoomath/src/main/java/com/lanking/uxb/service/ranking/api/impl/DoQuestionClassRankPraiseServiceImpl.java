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
import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassRankPraise;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.ranking.api.DoQuestionClassRankPraiseService;

import httl.util.CollectionUtils;

@Transactional(readOnly = true)
@Service
public class DoQuestionClassRankPraiseServiceImpl implements DoQuestionClassRankPraiseService {

	@Autowired
	@Qualifier("DoQuestionClassRankPraiseRepo")
	private Repo<DoQuestionClassRankPraise, Long> repo;

	@Override
	public DoQuestionClassRankPraise get(long id) {
		return repo.get(id);
	}

	@Override
	public Map<Long, DoQuestionClassRankPraise> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}

	@Override
	public Map<Long, DoQuestionClassRankPraise> getUserPraise(long userId) {
		Map<Long, DoQuestionClassRankPraise> data = new HashMap<>();
		List<DoQuestionClassRankPraise> praises = repo.find("$getUserClassPraise", Params.param("userId", userId))
				.list();
		if (CollectionUtils.isEmpty(praises)) {
			return data;
		}

		for (DoQuestionClassRankPraise v : praises) {
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
	public DoQuestionClassRankPraise getRankPraiseByRankId(long rankId, long userId) {
		Params param = Params.param();
		param.put("rankId", rankId);
		param.put("userId", userId);

		return repo.find("$getRankPraiseByRankId", param).get();
	}

	@Override
	public CursorPage<Long, DoQuestionClassRankPraise> getRankPraiseByRankId(long rankId,
			CursorPageable<Long> pageable) {
		return repo.find("$queryClassRankPraiseByCursor", Params.param("rankId", rankId)).fetch(pageable);
	}

}
