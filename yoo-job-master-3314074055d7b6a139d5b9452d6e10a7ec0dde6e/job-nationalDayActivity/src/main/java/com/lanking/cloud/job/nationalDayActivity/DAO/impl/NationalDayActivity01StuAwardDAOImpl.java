package com.lanking.cloud.job.nationalDayActivity.DAO.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01StuAward;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01StuAwardDAO;

@Component("nda01NationalDayActivity01StuAwardDAO")
public class NationalDayActivity01StuAwardDAOImpl extends AbstractHibernateDAO<NationalDayActivity01StuAward, Long>
		implements NationalDayActivity01StuAwardDAO {

	@Autowired
	@Qualifier("NationalDayActivity01StuAwardRepo")
	@Override
	public void setRepo(Repo<NationalDayActivity01StuAward, Long> repo) {
		this.repo = repo;
	}

	@Override
	public long count() {
		return repo.find("$nda01Count").count();
	}

}
