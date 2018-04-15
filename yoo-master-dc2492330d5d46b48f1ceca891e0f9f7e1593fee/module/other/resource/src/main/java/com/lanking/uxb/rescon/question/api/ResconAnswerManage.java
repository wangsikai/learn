package com.lanking.uxb.rescon.question.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Answer;

/**
 * 提供答案相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月14日
 */
public interface ResconAnswerManage {

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
