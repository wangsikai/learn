package com.lanking.uxb.rescon.question.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.QuestionCategory;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 题目类型，标签管理相关接口
 * 
 * @author sunming
 * @version 2017/7/24
 */
public interface ResconQuestionCategoryManage {

	/**
	 * 批量获取分类.
	 * 
	 * @param codes
	 * @return
	 */
	Map<Long, QuestionCategory> mget(Collection<Long> codes);

	/**
	 * 批量获取题目类型
	 * 
	 * @param questionIds
	 * @return 题目类型
	 */
	List<QuestionCategory> listAll(Status status);

	/**
	 * 新增修改题目类型
	 */
	QuestionCategory saveOrUpdateCategory(Long code, String name, Status status);

	/**
	 * 获取题目对应的分类.
	 * 
	 * @param questionId
	 *            习题ID
	 * @return
	 */
	List<QuestionCategory> listByQuestion(long questionId);

	/**
	 * 批量获取题目对应的分类.
	 * 
	 * @param questionIds
	 *            习题ID集合
	 * @return
	 */
	Map<Long, List<QuestionCategory>> mgetByQuestions(Collection<Long> questionIds);
}
