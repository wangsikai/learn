package com.lanking.uxb.service.diagnostic.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassKnowpoint;

public interface DiagnosticStudentClassKnowpointService {
	/**
	 * 根据等级查询不同知识体系统计
	 * 
	 * @param level
	 *            为null表示查全部
	 * @param studentId
	 * @param classId
	 * @return
	 */
	List<DiagnosticStudentClassKnowpoint> queryListByLevel(Integer level, Long studentId, Long classId);

	/**
	 * 根据知识专项查询对应的知识点统计情况
	 * 
	 * @param pcode
	 * @param studentId
	 * @param classId
	 * @return
	 */
	List<DiagnosticStudentClassKnowpoint> queryknowListBySpecial(Long pcode, Long studentId, Long classId);

	/**
	 * 
	 * @param code
	 * @param studentId
	 * @param classId
	 * @return
	 */
	DiagnosticStudentClassKnowpoint get(Long code, Long studentId, Long classId);

	/**
	 * 历史最低知识点掌握情况
	 * 
	 * @param pcode
	 * @param studentId
	 * @param classId
	 * @return
	 */
	List<DiagnosticStudentClassKnowpoint> queryHistoryWeakList(Long studentId, Long classId, Integer day0);

	/**
	 * 在codes里学生薄弱知识点查询
	 * 
	 * @param studentId
	 * @param classId
	 * @param codes
	 *            知识点
	 * @return
	 */
	List<DiagnosticStudentClassKnowpoint> queryHistoryWeakListByCodes(Long studentId, Long classId,
			Collection<Long> codes);

	/**
	 * 班级平均
	 * 
	 * @param pcode
	 * @param classId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> getClassAvgRightRateByPcode(Long pcode, Long classId);

	/**
	 * 根据当前教材下学生小专题列表
	 * 
	 * @param codes
	 *            知识点集合
	 * @param studentId
	 * @param classId
	 * @return
	 */
	List<DiagnosticStudentClassKnowpoint> querySamllTopicList(Collection<Long> codes, Long studentId, Long classId);

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
	 * 根据知识点
	 *
	 * @param code
	 *            知识点code
	 * @param studentId
	 *            学生id
	 * @return {@link List}
	 */
	List<DiagnosticStudentClassKnowpoint> queryByKnowledge(long code, long studentId);

	/**
	 * 获取第一层章节做题情况
	 * 
	 * @param sectionCodes
	 * @param studentId
	 * @return
	 */
	List<Map> getSectionDoCountMap(List<Long> sectionCodes, long studentId, Long classId);

	/**
	 * 对应最底层小节的知识点掌握情况
	 * 
	 * @param sectionCodes
	 * @param studentId
	 * @return
	 */
	List<Map> getKpDataBySectioncodes(List<Long> sectionCodes, long studentId, Long classId);
}
