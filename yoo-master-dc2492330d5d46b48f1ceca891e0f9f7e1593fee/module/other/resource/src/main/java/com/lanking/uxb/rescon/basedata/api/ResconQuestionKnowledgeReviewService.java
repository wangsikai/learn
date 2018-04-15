package com.lanking.uxb.rescon.basedata.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.KnowledgeReview;

/**
 * 题目和复习知识点关联接口
 * 
 * @wlche
 *
 */
public interface ResconQuestionKnowledgeReviewService {

	/**
	 * 根据习题获取新知识点集合.
	 * 
	 * @param questionId
	 *            习题ID
	 * @return
	 */
	List<KnowledgeReview> listByQuestion(Long questionId);

	/**
	 * 根据习题获取新知识点集合.
	 * 
	 * @param questionIds
	 *            习题ID集合
	 * @return
	 */
	Map<Long, List<KnowledgeReview>> mListByQuestions(Collection<Long> questionIds);

	/**
	 * 保存题目复习知识点.
	 * 
	 * @param questionId
	 *            习题ID
	 * @param knowledgeReviewIds
	 *            同步知识点ID
	 */
	void saveQuestionKnowledgeReview(long questionId, List<Long> knowledgeReviewIds);
}
