package com.lanking.uxb.service.diagnostic.api.student;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 学生诊断 - 班级全局所有作业知识点统计服务.
 * 
 * @author wangsenhao
 * 
 * @since 学生端，2017-6-30 数据跟随相关整理
 */
public interface StaticStuDiagnosticClassKpService {

	/**
	 * 统计时间范围内当前班级的知识点掌握情况,更新历史掌握情况<br>
	 * 业务变更:1.统计这个学生所在班级下的所有作业;2.学生被移除，加入的话要把历史数据重新算一遍<br>
	 * 对应表：diagno_stu_class_kp
	 * 
	 * @param startTime
	 * @param endTime
	 * @param studentId
	 */
	void statStuClassKp(Date startTime, Date endTime, Long studentId);

	/**
	 * 学生离开一个班级<br>
	 * 删除学生数据，重新计算
	 * 
	 * @param studentId
	 * @param classId
	 */
	void statWhenLeave(long studentId, long classId);

	/**
	 * 学生加入一个班级<br>
	 * 删除学生数据，重新计算
	 * 
	 * @param studentId
	 * @param classId
	 */
	void statWhenJoin(long studentId, long classId);

	/**
	 * 删除学生班级数据
	 * 
	 * @param studentId
	 * @param classId
	 */
	void deleteByStuId(long studentId, long classId);

	/**
	 * 查询学生作业信息
	 * 
	 * @param studentId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> findStuHkList(CursorPageable<Long> pageable, long studentId, Date startTime, Date endTime);

	/**
	 * 查询当前学生对应的有效班级
	 * 
	 * @param studentId
	 * @param classId
	 *            传值表示要过滤当前的，不传查全部的
	 * @return
	 */
	List<Long> findClassIds(long studentId, Long classId);

	/**
	 * 翻页获取学生
	 * 
	 * @param cursorPageable
	 * @return
	 */
	CursorPage<Long, Long> getAllStudent(CursorPageable<Long> cursorPageable);

	/**
	 * 下发的时候
	 * 
	 * @param classId
	 * @param hkId
	 */
	void statStuClassKp(long classId, Long hkId);

}
