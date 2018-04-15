package com.lanking.cloud.job.stuWeekReport.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.job.stuWeekReport.dao.HomeworkDAO;

@Component(value = "stuWeekReportHomeworkDAO")
public class HomeworkDAOImpl extends AbstractHibernateDAO<Homework, Long> implements HomeworkDAO {

	@Autowired
	@Qualifier("HomeworkRepo")
	@Override
	public void setRepo(Repo<Homework, Long> repo) {
		this.repo = repo;
	}
}
