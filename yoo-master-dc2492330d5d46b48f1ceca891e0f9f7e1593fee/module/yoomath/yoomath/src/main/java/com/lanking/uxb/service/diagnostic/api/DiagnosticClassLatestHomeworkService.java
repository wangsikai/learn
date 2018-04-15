package com.lanking.uxb.service.diagnostic.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassLatestHomework;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassLatestHomework;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface DiagnosticClassLatestHomeworkService {
	/**
	 * 根据班级id查找最新数据情况
	 *
	 * @param classId
	 *            班级id
	 * @return {@link List}
	 */
	List<DiagnosticClassLatestHomework> findByClassId(long classId, int times);

	/**
	 * 批量获取作业平均提交率
	 * 
	 * @param homeworkIds
	 * @return
	 */
	Map<Long, Double> getSubmitRate(List<Long> homeworkIds);

	/**
	 * 首页查询用户所有班级教师诊断数据，这里默认查最近30次的
	 * 
	 * @param classIds
	 * @param times
	 * @return
	 */
	Map<Long, List<VDiagnosticClassLatestHomework>> findByClassIds(List<Long> classIds, int times);
}
