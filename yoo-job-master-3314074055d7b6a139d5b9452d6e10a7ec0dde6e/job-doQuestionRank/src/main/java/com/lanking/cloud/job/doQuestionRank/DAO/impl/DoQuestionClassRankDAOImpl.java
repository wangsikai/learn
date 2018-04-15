package com.lanking.cloud.job.doQuestionRank.DAO.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassRank;
import com.lanking.cloud.job.doQuestionRank.DAO.DoQuestionClassRankDAO;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;

@Component
public class DoQuestionClassRankDAOImpl extends AbstractHibernateDAO<DoQuestionClassRank, Long>
		implements DoQuestionClassRankDAO {

	@Override
	public DoQuestionClassRank findStudentInClassRank(long classId, int startDate, int endDate, long userId) {
		Params param = Params.param();
		param.put("classId", classId);
		param.put("userId", userId);
		param.put("startDate", startDate);
		param.put("endDate", endDate);

		return repo.find("$findStudentInClassRank", param).get();
	}

	@Override
	public DoQuestionClassRank save(DoQuestionClassRank doQuestionClassRank) {
		return repo.save(doQuestionClassRank);
	}

	@Override
	public List<Long> getClassIds(int startDate, int endDate, int startindex, int size) {
		Params param = Params.param();
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("startindex", startindex);
		param.put("size", size);

		return repo.find("$findClassIds", param).list(Long.class);
	}

	@Override
	public List<DoQuestionClassRank> findStudentInClassRanks(List<Long> classIds, int startDate, int endDate) {
		Params param = Params.param();
		param.put("classIds", classIds);
		param.put("startDate", startDate);
		param.put("endDate", endDate);

		return repo.find("$findStudentInClassRanks", param).list();
	}

	@Override
	public List<DoQuestionClassRank> saves(List<DoQuestionClassRank> doQuestionClassRanks) {
		return repo.save(doQuestionClassRanks);
	}

	@Override
	public CursorPage<Long, DoQuestionClassRank> getAllRankPraiseByCursor(int startDate, int endDate,
			CursorPageable<Long> pageable) {
		Params param = Params.param();
		param.put("startDate", startDate);
		param.put("endDate", endDate);

		return repo.find("$findAllRankPraiseByCursor", param).fetch(pageable);
	}

	@Autowired
	@Qualifier("DoQuestionClassRankRepo")
	@Override
	public void setRepo(Repo<DoQuestionClassRank, Long> repo) {
		this.repo = repo;
	}

}
