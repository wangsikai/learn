package com.lanking.cloud.job.paperReport.dao;

import java.util.List;

public interface HomeworkStudentClazzDAO {

	/**
	 * 获取当前班级对应的学生id集合
	 * 
	 * @param classId
	 * @return
	 */
	List<Long> findStudentIdsByClassId(Long classId);
}
