package com.lanking.uxb.service.data.api;


/**
 * 学生班级的相关统计
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月21日
 */
public interface HomeworkStudentClazzStatService {

	/**
	 * 统计最近30天班级学生的作业情况
	 * 
	 * @since yoomath V1.4
	 * @param classId
	 *            班级ID
	 */
	void statisticDay30(long classId);

}
