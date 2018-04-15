package com.lanking.uxb.service.diagnostic.api;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;

/**
 * 学生班级接口.
 * 
 * @author wlche
 *
 */
public interface StaticHomeworkStudentClazzService {

	/**
	 * 获取最近的一个学生退出班级数据.
	 * 
	 * @param classId
	 *            班级
	 * @param studentId
	 *            学生
	 * @return
	 */
	HomeworkStudentClazz getByStudentExit(long classId, long studentId);
}
