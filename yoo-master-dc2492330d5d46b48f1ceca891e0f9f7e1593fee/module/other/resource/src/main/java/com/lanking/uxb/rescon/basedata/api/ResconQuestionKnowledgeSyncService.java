package com.lanking.uxb.rescon.basedata.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.KnowledgeSync;

/**
 * 题目和同步知识点关联接口
 * 
 * @wlche
 *
 */
public interface ResconQuestionKnowledgeSyncService {

	/**
	 * 根据习题获取新知识点集合.
	 * 
	 * @param questionId
	 *            习题ID
	 * @return
	 */
	List<KnowledgeSync> listByQuestion(Long questionId);

	/**
	 * 根据习题获取新知识点集合.
	 * 
	 * @param questionIds
	 *            习题ID集合
	 * @return
	 */
	Map<Long, List<KnowledgeSync>> mListByQuestions(Collection<Long> questionIds);

	/**
	 * 保存题目同步知识点.
	 * 
	 * @param questionId
	 *            习题ID
	 * @param knowledgeSyncIds
	 *            同步知识点ID
	 */
	void saveQuestionKnowledgeSync(long questionId, List<Long> knowledgeSyncIds);
}
