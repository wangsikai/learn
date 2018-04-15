package com.lanking.uxb.service.ranking.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolRank;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolRankPraise;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.ranking.api.DoQuestionSchoolRankService;

@Transactional(readOnly = true)
@Service
public class DoQuestionSchoolRankServiceImpl implements DoQuestionSchoolRankService {

	@Autowired
	@Qualifier("DoQuestionSchoolRankRepo")
	private Repo<DoQuestionSchoolRank, Long> doQuestionSchoolRankRepo;
	@Autowired
	@Qualifier("DoQuestionSchoolRankPraiseRepo")
	private Repo<DoQuestionSchoolRankPraise, Long> doQuestionSchoolRankPraiseRepo;

	@Override
	public DoQuestionSchoolRank get(long id) {
		return doQuestionSchoolRankRepo.get(id);
	}

	@Override
	public Map<Long, DoQuestionSchoolRank> mget(Collection<Long> ids) {
		return doQuestionSchoolRankRepo.mget(ids);
	}

	@Override
	public List<DoQuestionSchoolRank> listDoQuestionSchoolRankTopN(long schoolId, int startDate, int endDate,
			int topN) {
		Params param = Params.param();
		param.put("schoolId", schoolId);
		param.put("topN", topN);
		param.put("startDate", startDate);
		param.put("endDate", endDate);

		return doQuestionSchoolRankRepo.find("$listDoQuestionSchoolRank", param).list();
	}

	@Override
	public DoQuestionSchoolRank findStudentInSchoolRank(long schoolId, int startDate, int endDate, long userId) {
		Params param = Params.param();
		param.put("schoolId", schoolId);
		param.put("userId", userId);
		param.put("startDate", startDate);
		param.put("endDate", endDate);

		return doQuestionSchoolRankRepo.find("$findStudentInSchoolRank", param).get();
	}

	@Transactional(readOnly = false)
	@Override
	public void updateSchoolPraiseCount(long rankId, long userId) {
		DoQuestionSchoolRankPraise value = new DoQuestionSchoolRankPraise();
		value.setRankId(rankId);
		value.setUserId(userId);
		doQuestionSchoolRankPraiseRepo.save(value);

		Params param = Params.param();
		param.put("rankId", rankId);
		param.put("userId", userId);
		doQuestionSchoolRankRepo.execute("$updateSchoolPraiseCount", param);
	}

	@Transactional(readOnly = false)
	@Override
	public void cancelSchoolPraise(long rankId, long userId, long praiseId) {
		doQuestionSchoolRankPraiseRepo.deleteById(praiseId);

		Params param = Params.param();
		param.put("rankId", rankId);
		param.put("userId", userId);
		doQuestionSchoolRankRepo.execute("$cancelSchoolPraiseCount", param);
	}

}
