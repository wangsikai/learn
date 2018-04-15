package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lanking.cloud.domain.common.baseData.Section;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface KnowledgeSectionService {
	/**
	 * 根据章节找到对应的知识点
	 *
	 * @param code
	 *            章节代码
	 * @return 知识点列表
	 */
	List<Long> getBySection(long code);

	/**
	 * 根据教材码查询知识点列表
	 *
	 * @param code
	 *            教材码
	 * @return 知识点列表
	 */
	List<Long> getByTextbook(int code);

	/**
	 * 根据知识点查询对应教材
	 *
	 * @param codes
	 *            知识点code
	 * @return 教材代码列表
	 */
	List<Integer> queryTextbookByKnowledge(Collection<Long> codes);

	/**
	 * 根据知识点查询对应教材关系
	 *
	 * @param codes
	 *            知识点code
	 * @return 教材代码列表
	 */
	Map<Long, Set<Integer>> queryTextbookByKnowledgeRelation(Collection<Long> codes);

	/**
	 * 根据知识点及教材版本查章节.
	 * 
	 * @param KnowledgeCodes
	 *            知识点集合
	 * @param textbookCategoryCode
	 *            教材版本
	 * @return
	 */
	List<Section> findSectionsByMetaknowCodes(Collection<Long> KnowledgeCodes, Integer textbookCategoryCode);

	/**
	 * 根据知识点及教材版本查章节.PS:上面老接口没动,上面是当textbookCategoryCode 存在时
	 * 
	 * @param KnowledgeCodes
	 *            知识点集合
	 * @param textbookCategoryCode
	 *            教材版本
	 * @param direction
	 *            排序
	 * @param Limit
	 *            条数
	 * @return
	 */
	List<Section> findSections(Collection<Long> KnowledgeCodes, Integer textbookCategoryCode);

	/**
	 * 获得指定章节的知识点.
	 * 
	 * @param code
	 * @return
	 */
	List<Long> findBySectionCode(long code);

	/**
	 * 批量获得指定章节查询对应的知识点
	 * 
	 * @param sections
	 * @return
	 */
	Map<Long, List<Long>> mGetKnowledgeSectionMap(Collection<Long> sectionCodes);

	/**
	 * 根据知识点集合查询知识点章节对应关系.
	 * 
	 * @param KnowledgeCodes
	 *            知识点集合
	 * @return
	 */
	Map<Long, Set<Section>> findSectionRelationByKnowledgeCodes(Collection<Long> knowledgeCodes);
}
