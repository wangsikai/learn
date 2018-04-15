package com.lanking.cloud.job.doQuestionRank.DAO.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolRank;
import com.lanking.cloud.job.doQuestionRank.DAO.DoQuestionSchoolRankDAO;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;

@Component
public class DoQuestionSchoolRankDAOImpl extends AbstractHibernateDAO<DoQuestionSchoolRank, Long>
		implements DoQuestionSchoolRankDAO {

	@Override
	public DoQuestionSchoolRank findStudentInSchoolRank(long schoolId, int startDate, int endDate, long userId) {
		Params param = Params.param();
		param.put("schoolId", schoolId);
		param.put("userId", userId);
		param.put("startDate", startDate);
		param.put("endDate", endDate);

		return repo.find("$findStudentInSchoolRank", param).get();
	}

	@Override
	public DoQuestionSchoolRank save(DoQuestionSchoolRank doQuestionSchoolRank) {
		return repo.save(doQuestionSchoolRank);
	}

	@Override
	public List<Long> getSchoolIds(int startDate, int endDate, int startindex, int size) {
		Params param = Params.param();
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("startindex", startindex);
		param.put("size", size);

		return repo.find("$findSchoolIds", param).list(Long.class);
	}

	@Override
	public CursorPage<Long, DoQuestionSchoolRank> getSchoolRankPraiseBySchoolId(int startDate, int endDate,
			Long schoolId, CursorPageable<Long> pageable) {
		Params param = Params.param();
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		if (schoolId != null) {
			param.put("schoolId", schoolId);
		}

		return repo.find("$findSchoolRankPraiseBySchoolId", param).fetch(pageable);
	}

	@Override
	public List<DoQuestionSchoolRank> saves(List<DoQuestionSchoolRank> doQuestionSchoolRanks) {
		return repo.save(doQuestionSchoolRanks);
	}

	@Autowired
	@Qualifier("DoQuestionSchoolRankRepo")
	@Override
	public void setRepo(Repo<DoQuestionSchoolRank, Long> repo) {
		this.repo = repo;
	}

}
