package com.lanking.uxb.service.diagnostic.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassLatestHomeworkKnowpoint;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticStudentClassLatestHomeworkKnowpoint;

/**
 * 学生诊断班级作业知识点相关统计接口(最近30次)
 * 
 * @author wangsenhao
 *
 */
public interface DiagnosticStudentClassLatestHomeworkKnowpointService {
	/**
	 * 查询薄弱知识点
	 * 
	 * @param studentId
	 * @param classId
	 * @param times
	 * @param kpCount
	 *            知识点个数
	 * @return
	 */
	List<DiagnosticStudentClassLatestHomeworkKnowpoint> queryWeakList(Long studentId, Long classId, Integer times,
			int kpCount);

	/**
	 * 根据当前教材下学生小专题列表
	 * 
	 * @param codes
	 *            知识点集合
	 * @param studentId
	 * @param classId
	 * @return
	 */
	List<DiagnosticStudentClassLatestHomeworkKnowpoint> querySamllTopicList(Collection<Long> codes, Long studentId,
			Long classId);

	/**
	 * 小专题的班级平均正确率
	 * 
	 * @param codes
	 * @param classId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> querySmallTopicClassRateList(Collection<Long> codes, Long classId);

	/**
	 * 根据上级code查询对应下级的知识点信息集合
	 * 
	 * @param classId
	 * @param studentId
	 * @param pcode
	 * @return
	 */
	List<DiagnosticStudentClassLatestHomeworkKnowpoint> queryKnowledgeListByPcode(Long classId, Long studentId,
			Long pcode);

	/**
	 * 根据上级code查询对应下级知识专项或知识点对应的班级平均正确率
	 * 
	 * @param classId
	 * @param studentId
	 * @param pcode
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> getClassAvgRightRateByPcode(Long classId, Long pcode);

	/**
	 * 批量获取薄弱知识点
	 * 
	 * @param studentId
	 * @param classIds
	 * @param times
	 * @return
	 */
	Map<Long, List<VDiagnosticStudentClassLatestHomeworkKnowpoint>> findByClassIds(Long studentId, List<Long> classIds,
			Integer times);

}
