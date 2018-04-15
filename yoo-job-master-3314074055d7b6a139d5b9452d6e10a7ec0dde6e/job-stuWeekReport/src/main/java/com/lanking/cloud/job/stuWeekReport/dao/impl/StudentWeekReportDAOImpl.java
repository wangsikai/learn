package com.lanking.cloud.job.stuWeekReport.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.stat.StudentWeekReport;
import com.lanking.cloud.job.stuWeekReport.dao.StudentWeekReportDAO;
import com.lanking.cloud.sdk.data.Params;

@Component
public class StudentWeekReportDAOImpl extends AbstractHibernateDAO<StudentWeekReport, Long> implements
		StudentWeekReportDAO {

	@Autowired
	@Qualifier("StudentWeekReportRepo")
	@Override
	public void setRepo(Repo<StudentWeekReport, Long> repo) {
		this.repo = repo;
	}

	@Override
	public StudentWeekReport getByUser(Long userId, String startDate, String endDate) {
		return repo.find("$getByUser",
				Params.param("userId", userId).put("startDate", startDate).put("endDate", endDate)).get();
	}
}
