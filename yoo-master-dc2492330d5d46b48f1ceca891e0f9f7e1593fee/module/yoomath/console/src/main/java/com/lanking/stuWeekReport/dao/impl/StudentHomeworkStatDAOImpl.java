package com.lanking.stuWeekReport.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStat;

@Component(value = "stuWeekReportStudentHomeworkStatDAO")
public class StudentHomeworkStatDAOImpl extends AbstractHibernateDAO<StudentHomeworkStat, Long> implements
		com.lanking.stuWeekReport.dao.StudentHomeworkStatDAO {

	@Autowired
	@Qualifier("StudentHomeworkStatRepo")
	@Override
	public void setRepo(Repo<StudentHomeworkStat, Long> repo) {
		this.repo = repo;
	}

}
