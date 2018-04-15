package com.lanking.uxb.service.diagnostic.api;

import java.util.Map;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClass;

/**
 * 分析与练习建议(最近30次)
 * 
 * @author wangsenhao
 *
 */
public interface DiagnosticStudentClassService {
	/**
	 * 根据教材查询
	 * 
	 * @param textbookCode
	 * @param studentId
	 * @param classId
	 * @return
	 */
	DiagnosticStudentClass queryListByTextbookCode(Long textbookCode, Long studentId, Long classId);

	/**
	 * 获取班级平均练习数和平均正确数(冲刺题、提高题、基础题)
	 * 
	 * @param textbookCode
	 * @param classId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map getClassAvgDoQuestion(Long textbookCode, Long classId);

}
