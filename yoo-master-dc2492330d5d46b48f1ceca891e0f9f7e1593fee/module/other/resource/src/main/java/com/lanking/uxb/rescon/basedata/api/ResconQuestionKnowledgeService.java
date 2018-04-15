package com.lanking.uxb.rescon.basedata.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.KnowledgePoint;

/**
 * 题目和新知识点关联接口
 * 
 * @since 2.0.1
 * @author wangsenhao
 *
 */
public interface ResconQuestionKnowledgeService {
	/**
	 * 获取知识点集合对应的id集合
	 * 
	 * @param knowpointCodes
	 * @return
	 */
	List<Long> findQuestionIds(List<Long> knowpointCodes, Long vendorId);

	/**
	 * 根据习题获取新知识点集合.
	 * 
	 * @param questionId
	 *            习题ID
	 * @return
	 */
	List<KnowledgePoint> listByQuestion(Long questionId);

	/**
	 * 根据习题获取新知识点集合.
	 * 
	 * @param questionIds
	 *            习题ID集合
	 * @return
	 */
	Map<Long, List<KnowledgePoint>> mListByQuestions(Collection<Long> questionIds);
}
