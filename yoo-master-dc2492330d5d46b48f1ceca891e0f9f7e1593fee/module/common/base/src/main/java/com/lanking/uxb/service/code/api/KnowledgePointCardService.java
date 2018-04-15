package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.card.KnowledgePointCard;

/**
 * 知识卡片接口
 * 
 * @since 2.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年8月22日
 */
public interface KnowledgePointCardService {

	KnowledgePointCard get(long id);

	List<KnowledgePointCard> findByKnowledgePoint(long knowledgePointCode);

	/**
	 * 统计知识点对应的通过的卡片数量
	 * 
	 * @param codes
	 * @return
	 */
	Map<Long, Long> statisByPoints(Collection<Long> codes);

	/**
	 * 找到传入的知识点有卡片的知识点
	 *
	 * @param codes
	 *            知识点列表
	 * @return {@link List}
	 */
	List<Long> findHasCardKnowledgePoint(Collection<Long> codes);

	/**
	 * 查询某个阶段第一个卡片(知识点排序)
	 *
	 * @param phaseCode
	 *            阶段
	 */
	KnowledgePointCard getFirstKnowpointCardByPhaseCode(Integer phaseCode);

	/**
	 * 根据知识点列表查找所有知识卡片.
	 * 
	 * @param knowledgePointCodes
	 *            知识点列表
	 * @return {@link List}
	 */
	List<KnowledgePointCard> findByKnowledgePoints(Collection<Long> knowledgePointCodes);

	/**
	 * 通过学科编码查询所有的知识点卡片.
	 * 
	 * @param subjectCode
	 *            学科编码
	 * @return
	 */
	List<KnowledgePointCard> findBySubject(int subjectCode);
}
