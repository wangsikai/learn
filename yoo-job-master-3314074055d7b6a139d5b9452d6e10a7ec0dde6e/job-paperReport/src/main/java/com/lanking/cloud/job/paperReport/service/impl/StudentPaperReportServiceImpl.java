package com.lanking.cloud.job.paperReport.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoomath.stat.StudentPaperReport;
import com.lanking.cloud.job.paperReport.dao.StudentPaperReportDAO;
import com.lanking.cloud.job.paperReport.service.StudentPaperReportService;

@Transactional(readOnly = true)
@Service
public class StudentPaperReportServiceImpl implements StudentPaperReportService {

	@Autowired
	private StudentPaperReportDAO stuPaperReportDAO;

	@Override
	public List<StudentPaperReport> findByRecord(long recordId) {
		return stuPaperReportDAO.findByRecord(recordId);
	}
}
