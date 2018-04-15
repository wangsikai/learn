package com.lanking.uxb.service.diagnostic.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassLatestHomework;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticStudentClassLatestHomework;

/**
 * 诊断学生班级最新作业接口(最近30次，15次，7次)
 * 
 * @author wangsenhao
 *
 */
public interface DiagnosticStudentClassLatestHomeworkService {
	/**
	 * 根据学生和班级查询对应的统计数据
	 * 
	 * @param studentId
	 * @param classId
	 * @param times
	 * @return
	 */
	List<DiagnosticStudentClassLatestHomework> queryStat(Long studentId, Long classId, int times);

	/**
	 * 批量查询学生班级对应的学习诊断数据(供首页显示)
	 * 
	 * @param studentId
	 * @param classIds
	 * @param times
	 * @return
	 */
	Map<Long, List<VDiagnosticStudentClassLatestHomework>> findByClassIds(Long studentId, List<Long> classIds, int times);

}
