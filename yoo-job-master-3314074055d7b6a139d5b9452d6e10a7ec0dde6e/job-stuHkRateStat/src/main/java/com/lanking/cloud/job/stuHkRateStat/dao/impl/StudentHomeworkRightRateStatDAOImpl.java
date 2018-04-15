package com.lanking.cloud.job.stuHkRateStat.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkRightRateStat;
import com.lanking.cloud.job.stuHkRateStat.dao.StudentHomeworkRightRateStatDAO;

@Component
public class StudentHomeworkRightRateStatDAOImpl extends AbstractHibernateDAO<StudentHomeworkRightRateStat, Long>
		implements StudentHomeworkRightRateStatDAO {

	@Override
	public StudentHomeworkRightRateStat save(StudentHomeworkRightRateStat stat) {
		return repo.save(stat);
	}

	@Autowired
	@Qualifier("StudentHomeworkRightRateStatRepo")
	@Override
	public void setRepo(Repo<StudentHomeworkRightRateStat, Long> repo) {
		this.repo = repo;
	}
}
