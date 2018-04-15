package com.lanking.uxb.service.diagnostic.api;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

public interface DiagStuClassLastestStatService {
	/**
	 * 统计学生最新
	 * 
	 * @param classId
	 */
	void statStuClassLastestData(Long classId, Long hkId);

	/**
	 * 全部历史统计,每天统计更新一次
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 */
	void statStuClassData(Date startTime, Date endTime, Long clazzId);

	/**
	 * 获取所有学生
	 * 
	 * @param cursorPageable
	 * @return
	 */
	CursorPage<Long, Student> getAllStudent(CursorPageable<Long> cursorPageable);

	/**
	 * 查询学生对应的班级
	 * 
	 * @param studentId
	 * @return
	 */
	List<Long> queryClazzIds(Long studentId);

	/**
	 * DiagnosticStudentClassKnowpoint 查询当前表数量
	 * 
	 * @return
	 */
	Long count();

	/**
	 * diagnosticStudentClassLatestHomeworkKnowpoint 表是否有数据
	 * 
	 * @return
	 */
	Long count2();

}
