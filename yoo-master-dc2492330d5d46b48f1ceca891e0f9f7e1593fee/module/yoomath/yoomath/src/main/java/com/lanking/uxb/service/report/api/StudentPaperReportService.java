package com.lanking.uxb.service.report.api;

import com.lanking.cloud.domain.yoomath.stat.StudentPaperReport;

/**
 * 学生纸质报告
 * 
 * @since 渠道1.1.4
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年10月9日
 */
public interface StudentPaperReportService {

	StudentPaperReport get(long reportId);
}
