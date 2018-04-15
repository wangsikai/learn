package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.KnowledgePoint;

/**
 * 新知识点
 * 
 * @author wangsenhao
 *
 */
public interface KnowledgePointService {
	/**
	 * 
	 * @param code
	 * @return
	 */
	KnowledgePoint get(Long code);

	/**
	 * 批量获取
	 * 
	 * @param codes
	 * @return
	 */
	Map<Long, KnowledgePoint> mget(Collection<Long> codes);

	/**
	 * 根据知识点id列表查询知识点列表
	 *
	 * @param codes
	 *            知识点id列表
	 * @return 知识点列表
	 */
	List<KnowledgePoint> mgetList(Collection<Long> codes);

	/**
	 * 通过科目获取知识点
	 * 
	 * @param subjectCode
	 * @return
	 */
	List<KnowledgePoint> findBySubject(Integer subjectCode);

	/**
	 * 通过知识点专项code查询对应的子知识点
	 * 
	 * @param pcode
	 * @return
	 */
	List<KnowledgePoint> findByPcode(Long pcode);

	/**
	 * 获取所有知识点
	 * 
	 * @param noHasCodes
	 *            排除的知识点
	 * @return
	 */
	List<KnowledgePoint> findAll(Collection<Long> noHasCodes);
}
