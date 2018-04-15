package com.lanking.uxb.rescon.basedata.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;

/**
 * 公用知识分类.
 * 
 * @since 2.0.1
 */
public interface ResconKnowledgeSystemService {
	/**
	 * 获取对应的知识列表
	 * 
	 * @param phaseCode
	 * @param subjectCode
	 * @param pcode
	 * @param level
	 * @return
	 */
	List<KnowledgeSystem> findAll(Integer phaseCode, Integer subjectCode, Long pcode, Integer level);

	/**
	 * 根据不同层级获取对应专题总数、小专题总数、知识专项总数
	 * 
	 * @param phaseCode
	 * @param subjectCode
	 * @return
	 */
	Map<Integer, Long> getStat(Integer phaseCode, Integer subjectCode);

	/**
	 * 获取知识体系
	 * 
	 * @param code
	 * @return
	 */
	KnowledgeSystem get(long code);

	/**
	 * 获取小专题下对应的知识列表
	 * 
	 * @param phaseCode
	 * @param subjectCode
	 * @param knowpointCode
	 * @return
	 */
	List<KnowledgeSystem> findSmallSpecial(Integer phaseCode, Integer subjectCode, Long knowpointCode);

	/**
	 * 模糊查询出包括当前code的所有知识体系数据
	 * 
	 * @param code
	 * @return
	 */
	List<KnowledgeSystem> findByCode(Long code);
}
