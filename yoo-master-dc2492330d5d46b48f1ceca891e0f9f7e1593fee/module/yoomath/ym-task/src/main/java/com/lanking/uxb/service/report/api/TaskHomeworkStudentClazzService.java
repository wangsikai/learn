package com.lanking.uxb.service.report.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;

/**
 * 学生班级相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月23日
 */
public interface TaskHomeworkStudentClazzService {
	List<HomeworkStudentClazz> list(long classId);
}
