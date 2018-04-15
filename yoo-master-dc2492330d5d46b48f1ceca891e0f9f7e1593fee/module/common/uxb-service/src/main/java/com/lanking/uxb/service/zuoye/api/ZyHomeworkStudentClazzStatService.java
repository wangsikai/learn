package com.lanking.uxb.service.zuoye.api;

import java.math.BigDecimal;
import java.util.List;

import com.lanking.cloud.domain.yoomath.stat.HomeworkStudentClazzStat;

/**
 * 学生班级的相关统计接口
 * 
 * @since yoomath V1.4
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月21日
 */
public interface ZyHomeworkStudentClazzStatService {
	/**
	 * 得到某个班级下的学生排名
	 *
	 * @param classId
	 *            班级id
	 * @param size
	 *            查询数据大小
	 * @return 数据
	 */
	List<HomeworkStudentClazzStat> findTopStudent(long classId, long size);

	/**
	 * 查找班级这个正确率的学生
	 * 
	 * @since 2.0.3
	 * @param classId
	 * @param days30RightRate
	 * @return
	 */
	List<HomeworkStudentClazzStat> findStudentByRightRate(long classId, BigDecimal days30RightRate);

	/**
	 * 根据学生id移除近30天数据
	 *
	 * @param studentId
	 *            学生id
	 * @param classId
	 *            班级id
	 */
	void removeByStudentId(long studentId, long classId);

	/**
	 * 恢复学生的近30天数据
	 *
	 * @param studentId
	 *            学生id
	 * @param classId
	 *            班级id
	 */
	void recoverByStudentId(long studentId, long classId);

}
