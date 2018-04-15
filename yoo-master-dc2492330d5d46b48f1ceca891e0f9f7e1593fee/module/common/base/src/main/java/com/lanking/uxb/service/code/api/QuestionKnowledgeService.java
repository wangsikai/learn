package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.QuestionKnowledge;

public interface QuestionKnowledgeService {
	/**
	 * 通过题目id查询知识点集合
	 * 
	 * @param questionId
	 * @return
	 */
	List<Long> queryKnowledgeByQuestionId(Long questionId);

	/**
	 * 清空题目对应新知识点缓存.
	 * 
	 * @param questionId
	 */
	void removeKnowledgeCacheByQuestionId(Long questionId);

	/**
	 * 批量获取知识点集合
	 * 
	 * @param questionIds
	 * @return
	 */
	Map<Long, List<Long>> mgetByQuestions(Collection<Long> questionIds);

	/**
	 * 查询传入知识点列表的所有父知识专项等数据id(非重复)
	 *
	 * @param codes
	 *            知识点code列表
	 * @return {@link List}
	 */
	List<Long> queryParentKnowledgeCodes(Collection<Long> codes);

	/**
	 * 查询每个知识点下的题目数量 knowledgeCode -> question count
	 *
	 * @param subjectCode
	 *            学科代码
	 * @return {@link Map}
	 */
	Map<Long, Long> queryKpQuestions(int subjectCode);

	/**
	 * 根据question id取对应该知识点列表
	 *
	 * @param questionIds
	 *            题目id列表
	 * @return {@link List}
	 */
	List<QuestionKnowledge> findByQuestions(Collection<Long> questionIds);
}
