package com.lanking.uxb.service.correct.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Answer;

/**
 * 批改流程使用的习题答案接口.
 * 
 * @author wanlong.che
 * @since 小优快批
 */
public interface CorrectAnswerService {

	/**
	 * 批量获取问题答案
	 * 
	 * @param questionIds
	 *            问题ID
	 * @return Map
	 */
	Map<Long, List<Answer>> getQuestionAnswers(Collection<Long> questionIds);
}
