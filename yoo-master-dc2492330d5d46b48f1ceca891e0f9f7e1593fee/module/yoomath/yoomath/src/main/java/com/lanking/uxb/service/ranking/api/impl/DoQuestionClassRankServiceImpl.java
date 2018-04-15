package com.lanking.uxb.service.ranking.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassRank;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassRankPraise;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.ranking.api.DoQuestionClassRankService;

@Transactional(readOnly = true)
@Service
public class DoQuestionClassRankServiceImpl implements DoQuestionClassRankService {

	@Autowired
	@Qualifier("DoQuestionClassRankRepo")
	private Repo<DoQuestionClassRank, Long> doQuestionClassRankRepo;
	@Autowired
	@Qualifier("DoQuestionClassRankPraiseRepo")
	private Repo<DoQuestionClassRankPraise, Long> doQuestionClassRankPraiseRepo;

	@Override
	public DoQuestionClassRank get(long id) {
		return doQuestionClassRankRepo.get(id);
	}

	@Override
	public Map<Long, DoQuestionClassRank> mget(Collection<Long> ids) {
		return doQuestionClassRankRepo.mget(ids);
	}

	@Override
	public List<DoQuestionClassRank> listDoQuestionClassStatTopN(long classId, int startDate, int endDate, int topN) {
		Params param = Params.param();
		param.put("classId", classId);
		param.put("topN", topN);
		param.put("startDate", startDate);
		param.put("endDate", endDate);

		return doQuestionClassRankRepo.find("$listDoQuestionClassStat", param).list();
	}

	@Override
	public DoQuestionClassRank findStudentInClassRank(long classId, int startDate, int endDate, long userId) {
		Params param = Params.param();
		param.put("classId", classId);
		param.put("userId", userId);
		param.put("startDate", startDate);
		param.put("endDate", endDate);

		return doQuestionClassRankRepo.find("$findStudentInClassRank", param).get();
	}

	@Transactional(readOnly = false)
	@Override
	public void updateClassPraiseCount(long rankId, long userId) {
		DoQuestionClassRankPraise value = new DoQuestionClassRankPraise();
		value.setRankId(rankId);
		value.setUserId(userId);
		doQuestionClassRankPraiseRepo.save(value);

		Params param = Params.param();
		param.put("rankId", rankId);
		param.put("userId", userId);
		doQuestionClassRankRepo.execute("$updateClassPraiseCount", param);
	}

	@Transactional(readOnly = false)
	@Override
	public void cancelPraise(long rankId, long userId, long praiseId) {
		doQuestionClassRankPraiseRepo.deleteById(praiseId);

		Params param = Params.param();
		param.put("rankId", rankId);
		param.put("userId", userId);
		doQuestionClassRankRepo.execute("$cancelClassPraiseCount", param);
	}

}
