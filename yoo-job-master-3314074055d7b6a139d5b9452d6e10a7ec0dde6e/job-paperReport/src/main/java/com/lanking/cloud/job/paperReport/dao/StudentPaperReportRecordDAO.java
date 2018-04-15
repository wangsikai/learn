package com.lanking.cloud.job.paperReport.dao;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.yoomath.stat.StudentPaperReportRecord;

public interface StudentPaperReportRecordDAO {

	/**
	 * 获取需要生成数据和失败的记录，失败的也需要重新跑
	 * 
	 * @return
	 */
	List<StudentPaperReportRecord> findDataProductingList();

	StudentPaperReportRecord save(StudentPaperReportRecord record);

	/**
	 * 获取可以生成pdf的记录.
	 * 
	 * @return
	 */
	List<StudentPaperReportRecord> findDataToFileList();

	/**
	 * 生成文件.
	 * 
	 * @param recordIds
	 *            记录
	 */
	void successFile(Collection<Long> recordIds);
}
