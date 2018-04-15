package com.lanking.cloud.job.paperReport.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.job.paperReport.dao.HomeworkClazzDAO;

@Component(value = "paperReportHomeworkClazzDAO")
public class HomeworkClazzDAOImpl extends AbstractHibernateDAO<HomeworkClazz, Long> implements HomeworkClazzDAO {

	@Autowired
	@Qualifier("HomeworkClazzRepo")
	@Override
	public void setRepo(Repo<HomeworkClazz, Long> repo) {
		this.repo = repo;
	}
}
