package com.lanking.uxb.rescon.question.api;

import java.util.Collection;
import java.util.Map;

/**
 * v3知识点转换相关接口.
 * 
 * @author wlche
 *
 */
public interface ResconKnowledgeV3Service {

	/**
	 * 搜索书本未转换的习题个数.
	 * 
	 * @param bookVersionIds
	 *            版本ID集合
	 * @return
	 */
	Map<Long, Integer> findVersionNoV3Counts(Collection<Long> bookVersionIds);

	/**
	 * 搜索试卷未转换的习题个数.
	 * 
	 * @param examPaperIds
	 *            试卷ID集合
	 * @return
	 */
	Map<Long, Integer> findPaperNoV3Counts(Collection<Long> examPaperIds);

	/**
	 * 搜索书本未转换的习题个数.
	 * 
	 * @param versionId
	 *            版本
	 * @return
	 */
	int findVersionNoV3Count(long versionId);

	/**
	 * 搜索试卷未转换的习题个数.
	 * 
	 * @param examPaperId
	 *            试卷ID
	 * @return
	 */
	int findPaperNoV3Count(long examPaperId);

	/**
	 * 搜索章节未转换的习题个数
	 * 
	 * @param versionId
	 *            版本ID
	 * @param catalogIds
	 *            目录ID
	 * @return
	 */
	Map<Long, Integer> findCatalogNoV3Counts(long versionId, Collection<Long> catalogIds);

	/**
	 * 获取旧知识点下未处理题目个数（第一级）.
	 * 
	 * @param subjectCode
	 *            学科阶段
	 * @param vendorId
	 *            供应商
	 * @return
	 */
	Map<Integer, Integer> calNoKnowledgePointL1(int subjectCode, long vendorId);

	/**
	 * 查询需要更新V3知识点的习题个数
	 * 
	 * @param vendorId
	 * @return
	 */
	long getNoHasV3KPQuestionCount(long vendorId, Integer phaseCode);

	/**
	 * 查询同步知识点的习题个数
	 * 
	 * @param knowledgeCode
	 *            知识点
	 * @return
	 */
	long getSyncKPQuestionCount(long vendorId, Long knowledgeCode, Integer phaseCode);

	/**
	 * 查询复习知识点的习题个数
	 * 
	 * @param knowledgeCode
	 *            知识点
	 * @return
	 */
	long getReviewKPQuestionCount(long vendorId, Long knowledgeCode, Integer phaseCode);
}
