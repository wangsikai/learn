package com.lanking.uxb.service.report.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReport;
import com.lanking.uxb.service.report.api.StudentPaperReportService;

@Transactional(readOnly = true)
@Service
public class StudentPaperReportServiceImpl implements StudentPaperReportService {

	@Autowired
	@Qualifier("StudentPaperReportRepo")
	private Repo<StudentPaperReport, Long> studentPaperReportRepo;

	@Override
	public StudentPaperReport get(long reportId) {
		return studentPaperReportRepo.get(reportId);
	}

}
