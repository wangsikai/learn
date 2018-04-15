package com.lanking.uxb.channelSales.report.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReport;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.channelSales.report.api.CsStudentPaperReportService;

/**
 * 学情纸质报告相关接口实现.
 * 
 * @author wlche
 *
 */
@Service
@Transactional(readOnly = true)
public class CsStudentPaperReportServiceImpl implements CsStudentPaperReportService {

	@Autowired
	@Qualifier("StudentPaperReportRepo")
	private Repo<StudentPaperReport, Long> studentPaperReportRepo;

	@Override
	public List<StudentPaperReport> listByRecord(long recordId) {
		return studentPaperReportRepo.find("$listByRecord", Params.param("recordId", recordId)).list();
	}
}
