package com.lanking.uxb.channelSales.report.api;

import com.lanking.cloud.domain.yoomath.stat.StudentPaperReportRecord;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.channelSales.report.form.StudentReportPaperForm;

/**
 * 学情纸质报告记录相关接口.
 * 
 * @author wlche
 *
 */
public interface CsStudentPaperReportRecordService {

	/**
	 * 获取记录.
	 * 
	 * @param id
	 * @return
	 */
	StudentPaperReportRecord get(long id);

	/**
	 * 获取最近的一条记录.
	 * 
	 * @param classId
	 *            班级ID
	 * @return
	 */
	StudentPaperReportRecord getLatestRecord(long classId);

	/**
	 * 生成报告.
	 * 
	 * @param form
	 *            参数
	 */
	void createStudentReportPaper(StudentReportPaperForm form, long operator);

	/**
	 * 未读消息数.
	 * 
	 * @param channelCode
	 *            渠道编号
	 * @return
	 */
	long countChannelNotRead(int channelCode);

	/**
	 * 获取报告记录.
	 * 
	 * @param channelCode
	 *            渠道号
	 * @param pageable
	 *            分页信息
	 * @return
	 */
	Page<StudentPaperReportRecord> queryRecords(int channelCode, Pageable pageable);

	/**
	 * 下载报告处理.
	 * 
	 * @param recordId
	 *            报告ID
	 */
	void dowloadRecord(long recordId);
}
