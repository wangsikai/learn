package com.lanking.cloud.job.correctUserDayStat.DAO.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.job.correctUserDayStat.DAO.CorrectUserMonthStatDAO;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.microservice.domain.yoocorrect.CorrectUserMonthStat;

@Component(value = "StatCorrectUserMonthStatDAO")
public class CorrectUserMonthStatDAOImpl extends AbstractHibernateDAO<CorrectUserMonthStat, Long>
		implements CorrectUserMonthStatDAO {

	@Autowired
	@Qualifier("CorrectUserMonthStatRepo")
	@Override
	public void setRepo(Repo<CorrectUserMonthStat, Long> repo) {
		this.repo = repo;
	}
	
	@Override
	public List<CorrectUserMonthStat> getAllByUser(Long userId) {
		return repo.find("$queryAllByUser", Params.param("userId", userId)).list();
	}

	@Override
	public CorrectUserMonthStat getByMonthDate(Long userId, Date monthDate) {
		Params params = Params.param();
		params.put("userId", userId);
		params.put("monthDate", monthDate);

		return repo.find("$queryByMonthDate", params).get();
	}
}
