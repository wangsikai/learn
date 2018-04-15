package com.lanking.cloud.job.paperReport.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReportRecord;

public interface StudentPaperReportRecordService {

	int productData();

	List<StudentPaperReportRecord> findDataToFileList();

	/**
	 * 完成文件输出.
	 * 
	 * @param recordIds
	 *            记录ID集合
	 */
	void successFile(Collection<Long> recordIds);

	/**
	 * 批量获取班级信息.
	 * 
	 * @param classIds
	 *            班级ID
	 * @return
	 */
	Map<Long, HomeworkClazz> mgetHomeworkClazz(Collection<Long> classIds);

	/**
	 * 批量获取学生信息.
	 * 
	 * @param studentIds
	 *            学生ID
	 * @return
	 */
	Map<Long, Student> mgetStudent(Collection<Long> studentIds);
}
