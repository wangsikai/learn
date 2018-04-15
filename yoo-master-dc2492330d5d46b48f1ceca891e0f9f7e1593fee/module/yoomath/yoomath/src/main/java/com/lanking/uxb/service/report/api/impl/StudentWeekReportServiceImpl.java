package com.lanking.uxb.service.report.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.StudentWeekReport;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.report.api.StudentWeekReportService;

@Transactional(readOnly = true)
@Service
public class StudentWeekReportServiceImpl implements StudentWeekReportService {

	@Autowired
	@Qualifier("StudentWeekReportRepo")
	private Repo<StudentWeekReport, Long> repo;

	@Override
	public List<StudentWeekReport> getByUserId(Long userId) {
		return repo.find("$getByUserId", Params.param("userId", userId)).list();
	}

	@Override
	public StudentWeekReport findWeekReport(Long userId, String startDate, String endDate) {
		Params params = Params.param("userId", userId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return repo.find("$findWeekReport", params).get();
	}

}
