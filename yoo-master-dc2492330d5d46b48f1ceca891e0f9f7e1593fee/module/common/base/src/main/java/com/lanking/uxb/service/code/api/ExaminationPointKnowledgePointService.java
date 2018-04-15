package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.ExaminationPointKnowledgePoint;

public interface ExaminationPointKnowledgePointService {

	List<ExaminationPointKnowledgePoint> findByKnowledgePoint(long knowledgePointCode);

	/**
	 * 保存考点知识点关联.
	 * 
	 * @param examinationPointID
	 *            考点
	 * @param knowpointCodes
	 *            知识点集合
	 */
	void save(long examinationPointID, Collection<Long> knowpointCodes);

	/**
	 * 根据考点查询关联
	 * 
	 * @param examinationPointCode
	 *            考点
	 * @return
	 */
	List<ExaminationPointKnowledgePoint> findByExaminationPoint(long examinationPointCode);

	/**
	 * 根据考点查询关联
	 * 
	 * @param examinationPointCodes
	 *            考点集合
	 * @return
	 */
	Map<Long, List<ExaminationPointKnowledgePoint>> findByExaminationPoints(Collection<Long> examinationPointCodes);
}
