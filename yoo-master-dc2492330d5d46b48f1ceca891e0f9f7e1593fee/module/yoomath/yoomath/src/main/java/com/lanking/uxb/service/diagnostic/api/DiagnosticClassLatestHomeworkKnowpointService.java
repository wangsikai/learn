package com.lanking.uxb.service.diagnostic.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassLatestHomeworkKnowpoint;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassLatestHomeworkKnowpoint;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface DiagnosticClassLatestHomeworkKnowpointService {
	/**
	 * 分页查询最新30/15/7次作业知识点
	 *
	 * @param classId
	 * @param times
	 * @param kpCount
	 *            薄弱知识点查询的个数 (最近多少次) 班级id
	 * @return {@link Page}
	 */
	List<DiagnosticClassLatestHomeworkKnowpoint> findByPage(long classId, int times, int kpCount);

	/**
	 * 批量获取教学诊断最新30次数据
	 * 
	 * @param classIds
	 * @param times
	 * @return
	 */
	Map<Long, List<VDiagnosticClassLatestHomeworkKnowpoint>> findByClassIds(List<Long> classIds, int times);

	/**
	 * 通过班级和对应的知识点code查询对应的信息
	 * 
	 * @param studentId
	 * @param classId
	 * @param codes
	 * @return
	 */
	List<DiagnosticClassLatestHomeworkKnowpoint> findByCodes(long classId, List<Long> codes);
}
