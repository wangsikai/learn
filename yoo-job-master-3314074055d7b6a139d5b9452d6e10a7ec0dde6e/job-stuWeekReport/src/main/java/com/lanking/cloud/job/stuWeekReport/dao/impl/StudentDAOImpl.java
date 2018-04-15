package com.lanking.cloud.job.stuWeekReport.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.job.stuWeekReport.dao.StudentDAO;

@Component(value = "stuWeekReportStudentDAO")
public class StudentDAOImpl extends AbstractHibernateDAO<Student, Long> implements StudentDAO {

	@Autowired
	@Qualifier("StudentRepo")
	@Override
	public void setRepo(Repo<Student, Long> repo) {
		this.repo = repo;
	}

}
