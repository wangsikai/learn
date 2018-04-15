package com.lanking.uxb.service.zuoye.api;

import java.util.List;

import com.lanking.cloud.domain.support.resources.question.QuestionErrorType;

/**
 * 题目报错相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月6日
 */
public interface ZyQuestionErrorService {
	/**
	 * 保存纠错
	 * 
	 * @param description
	 *            描述
	 * @param types
	 *            纠错类型
	 * @param questionId
	 *            题目id
	 * @param userId
	 *            用户id
	 */
	void saveError(String description, List<QuestionErrorType> types, Long questionId, Long userId);
}
