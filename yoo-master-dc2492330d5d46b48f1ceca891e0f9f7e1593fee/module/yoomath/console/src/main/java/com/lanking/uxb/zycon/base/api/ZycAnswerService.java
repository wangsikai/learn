package com.lanking.uxb.zycon.base.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Answer;

/**
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年8月31日 上午11:11:54
 */
public interface ZycAnswerService {

	/**
	 * 获取题目答案
	 * 
	 * @param questionId
	 *            题目ID
	 * @return 题目答案集合
	 */
	List<Answer> getQuestionAnswers(long questionId);

	/**
	 * 批量获取问题答案
	 * 
	 * @param questionIds
	 *            问题ID
	 * @return Map
	 */
	Map<Long, List<Answer>> getQuestionAnswers(Collection<Long> questionIds);
}
