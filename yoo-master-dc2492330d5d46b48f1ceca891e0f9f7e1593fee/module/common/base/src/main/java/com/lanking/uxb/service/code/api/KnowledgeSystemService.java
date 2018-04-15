package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;

public interface KnowledgeSystemService {
	/**
	 * 
	 * @param code
	 * @return
	 */
	KnowledgeSystem get(Long code);

	/**
	 * 根据等级和学科获得数据
	 *
	 * @param level
	 *            级别
	 * @param subjectCode
	 *            学科
	 * @return 列表
	 */
	List<KnowledgeSystem> getBySubjectAndLevel(int level, int subjectCode);

	/**
	 * 批量获取
	 * 
	 * @param keys
	 * @return
	 */
	Map<Long, KnowledgeSystem> mget(Collection<Long> keys);

	/**
	 * 获取所有知识体系
	 * 
	 * @return
	 */
	List<KnowledgeSystem> findAll();

	/**
	 * 通过科目查询对应的知识体系
	 * 
	 * @param subjectCode
	 * @return
	 */
	List<KnowledgeSystem> findAllBySubject(Integer subjectCode);

	/**
	 * 查询知识点下的子节点
	 * 
	 * @param code
	 * 
	 * @return
	 */
	List<KnowledgeSystem> findChildByPcode(Long code);

	/**
	 * 通过知识点查询四层的code集合
	 * 
	 * @param code
	 *            子知识点
	 * @return
	 */
	List<Long> findAllCodeByKPoint(Long code);

	/**
	 * 通过知识点列表查找所有父层知识点集合
	 *
	 * @param codes
	 *            知识点集合
	 * @return {@link List}
	 */
	List<Long> findAllCodeByKPoint(Collection<Long> codes);

	/**
	 * 根据code列表得到列表
	 *
	 * @param codes
	 *            {@link Collection}
	 * @return {@link List}
	 */
	List<KnowledgeSystem> mgetList(Collection<Long> codes);

}
