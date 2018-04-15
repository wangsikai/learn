package com.lanking.cloud.job.paperReport.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.job.paperReport.dao.TeacherDAO;

@Component(value = "paperReportTeacherDAO")
public class TeacherDAOImpl extends AbstractHibernateDAO<Teacher, Long> implements TeacherDAO {

	@Autowired
	@Qualifier("TeacherRepo")
	@Override
	public void setRepo(Repo<Teacher, Long> repo) {
		this.repo = repo;
	}
}
