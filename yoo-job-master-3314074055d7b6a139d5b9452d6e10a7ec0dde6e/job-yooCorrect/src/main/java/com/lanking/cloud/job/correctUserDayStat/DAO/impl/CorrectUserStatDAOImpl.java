package com.lanking.cloud.job.correctUserDayStat.DAO.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.job.correctUserDayStat.DAO.CorrectUserStatDAO;
import com.lanking.microservice.domain.yoocorrect.CorrectUserStat;

@Component(value = "StatCorrectUserStatDAO")
public class CorrectUserStatDAOImpl extends AbstractHibernateDAO<CorrectUserStat, Long> implements CorrectUserStatDAO {

	@Autowired
	@Qualifier("CorrectUserStatRepo")
	@Override
	public void setRepo(Repo<CorrectUserStat, Long> repo) {
		this.repo = repo;
	}

}
