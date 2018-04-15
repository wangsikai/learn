package com.lanking.cloud.job.nationalDayActivity.DAO.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01TeaAward;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01TeaAwardDAO;

@Component("nda01NationalDayActivity01TeaAwardDAO")
public class NationalDayActivity01TeaAwardDAOImpl extends AbstractHibernateDAO<NationalDayActivity01TeaAward, Long>
		implements NationalDayActivity01TeaAwardDAO {

	@Autowired
	@Qualifier("NationalDayActivity01TeaAwardRepo")
	@Override
	public void setRepo(Repo<NationalDayActivity01TeaAward, Long> repo) {
		this.repo = repo;
	}

	@Override
	public long count() {
		return repo.find("$nda01TeaAwardCount").count();
	}

}
