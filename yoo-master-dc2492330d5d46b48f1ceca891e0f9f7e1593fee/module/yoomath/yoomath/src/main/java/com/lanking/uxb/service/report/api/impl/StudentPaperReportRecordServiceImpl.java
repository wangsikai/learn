package com.lanking.uxb.service.report.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReportRecord;
import com.lanking.uxb.service.report.api.StudentPaperReportRecordService;

@Transactional(readOnly = true)
@Service
public class StudentPaperReportRecordServiceImpl implements StudentPaperReportRecordService {
	@Autowired
	@Qualifier("StudentPaperReportRecordRepo")
	private Repo<StudentPaperReportRecord, Long> studentPaperReportRecordRepo;

	@Override
	public StudentPaperReportRecord get(long id) {
		return studentPaperReportRecordRepo.get(id);
	}

}
