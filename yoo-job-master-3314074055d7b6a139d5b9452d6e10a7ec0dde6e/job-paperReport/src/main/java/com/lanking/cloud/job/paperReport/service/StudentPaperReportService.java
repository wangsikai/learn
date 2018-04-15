package com.lanking.cloud.job.paperReport.service;

import java.util.List;

import com.lanking.cloud.domain.yoomath.stat.StudentPaperReport;

public interface StudentPaperReportService {

	/**
	 * 根据记录查找.
	 * 
	 * @param recordId
	 *            记录ID
	 * @return
	 */
	List<StudentPaperReport> findByRecord(long recordId);
}
