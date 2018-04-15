package com.lanking.cloud.job.correctUserDayStat.DAO.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.job.correctUserDayStat.DAO.CorrectBillsDAO;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.microservice.domain.yoocorrect.CorrectBills;

@Component(value = "StatCorrectBillsDAO")
public class CorrectBillsDAOImpl extends AbstractHibernateDAO<CorrectBills, Long> implements CorrectBillsDAO {

	@Autowired
	@Qualifier("CorrectBillsRepo")
	@Override
	public void setRepo(Repo<CorrectBills, Long> repo) {
		this.repo = repo;
	}

	@Override
	public List<CorrectBills> getByCorrectQuestionIds(List<Long> correctQuestionIds) {
		Params params = Params.param("correctQuestionIds", correctQuestionIds);
		return repo.find("$queryByCorrectQuestionIds", params).list();
	}

	@Override
	public Page<CorrectBills> query(Long correctUserId, Pageable page) {
		return repo.find("$query", Params.param("correctUserId", correctUserId)).fetch(page);
	}

	@Override
	public Long getCorrectErrorCount(Long correctUserId, List<Long> correctQuestionIds) {
		Params params = Params.param();
		params.put("correctUserId", correctUserId);
		params.put("correctQuestionIds", correctQuestionIds);
		return repo.find("$getCorrectErrorCount", params).count();
	}

}
