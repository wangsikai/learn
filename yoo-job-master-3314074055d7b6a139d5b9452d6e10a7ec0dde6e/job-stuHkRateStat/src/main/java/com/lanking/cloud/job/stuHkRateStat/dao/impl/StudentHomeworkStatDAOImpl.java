package com.lanking.cloud.job.stuHkRateStat.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStat;
import com.lanking.cloud.job.stuHkRateStat.dao.StudentHomeworkStatDAO;
import com.lanking.cloud.sdk.data.Params;

@Component
public class StudentHomeworkStatDAOImpl extends AbstractHibernateDAO<StudentHomeworkStat, Long> implements
		StudentHomeworkStatDAO {

	@Override
	public Integer getAvgRate(Long studentId) {
		return repo.find("$getAvgRate", Params.param("studentId", studentId)).get(Integer.class);
	}

	@Autowired
	@Qualifier("StudentHomeworkStatRepo")
	@Override
	public void setRepo(Repo<StudentHomeworkStat, Long> repo) {
		this.repo = repo;
	}
}
