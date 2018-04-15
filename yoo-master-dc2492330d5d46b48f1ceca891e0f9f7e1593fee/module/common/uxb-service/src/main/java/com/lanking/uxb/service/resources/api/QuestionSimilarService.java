package com.lanking.uxb.service.resources.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.QuestionSimilar;

/**
 * 前台展示用相似题服务.
 * 
 * @author wlche
 *
 */
public interface QuestionSimilarService {

	/**
	 * 获取相似题.
	 * 
	 * @param questionId
	 * @return
	 */
	QuestionSimilar getByQuestion(long questionId);

	/**
	 * 批量获取相似题.
	 * 
	 * @param questionIds
	 * @return
	 */
	Map<Long, QuestionSimilar> mGetByQuestion(Collection<Long> questionIds);
}
