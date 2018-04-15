package com.lanking.uxb.service.diagnostic.api;

import java.util.Collection;

import com.lanking.cloud.domain.yoomath.homework.Homework;

/**
 * 对班级掌握情况的相关统计
 *
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface StaticDiagnosticClassService {
	/**
	 * 根据普通作业进行统计
	 *
	 * @param id
	 *            班级id
	 */
	void staticByClass(long id, Long homeworkId);

	/**
	 * 根据假期作业进行统计
	 *
	 * @param id
	 *            假期作业id
	 */
	void staticByHolidayHomework(long id, long classId, long createId);

}
