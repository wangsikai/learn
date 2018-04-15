package com.lanking.cloud.job.stuWeekReport.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.stat.StudentClassWeekReport;
import com.lanking.cloud.job.stuWeekReport.dao.StudentClassWeekReportDAO;
import com.lanking.cloud.sdk.data.Params;

@Component
public class StudentClassWeekReportDAOImpl extends AbstractHibernateDAO<StudentClassWeekReport, Long> implements
		StudentClassWeekReportDAO {

	@Autowired
	@Qualifier("StudentClassWeekReportRepo")
	@Override
	public void setRepo(Repo<StudentClassWeekReport, Long> repo) {
		this.repo = repo;
	}

	@Override
	public StudentClassWeekReport getByUser(Long userId, Long classId, String startDate, String endDate) {
		Params params = Params.param("userId", userId);
		params.put("classId", classId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return repo.find("$getByUser", params).get();
	}

	@Override
	public List<StudentClassWeekReport> findList(List<Long> classIds, String startDate, String endDate) {
		Params params = Params.param("classIds", classIds);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return repo.find("$findList", params).list();
	}

}
