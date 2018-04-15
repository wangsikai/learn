package com.lanking.cloud.job.nationalDayActivity.DAO.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01DAO;

@Component("nda01NationalDayActivity01DAO")
public class NationalDayActivity01DAOImpl extends AbstractHibernateDAO<NationalDayActivity01, Long>
		implements NationalDayActivity01DAO {

	@Autowired
	@Qualifier("NationalDayActivity01Repo")
	@Override
	public void setRepo(Repo<NationalDayActivity01, Long> repo) {
		this.repo = repo;
	}

}
