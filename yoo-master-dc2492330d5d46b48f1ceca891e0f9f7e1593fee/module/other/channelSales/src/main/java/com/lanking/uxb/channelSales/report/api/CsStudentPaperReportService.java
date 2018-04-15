package com.lanking.uxb.channelSales.report.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.stat.StudentPaperReport;

/**
 * 学情纸质报告相关接口.
 * 
 * @author wlche
 *
 */
public interface CsStudentPaperReportService {

	/**
	 * 根据记录获取学生报告.
	 * 
	 * @param recordId
	 *            记录ID
	 * @return
	 */
	List<StudentPaperReport> listByRecord(long recordId);
}
