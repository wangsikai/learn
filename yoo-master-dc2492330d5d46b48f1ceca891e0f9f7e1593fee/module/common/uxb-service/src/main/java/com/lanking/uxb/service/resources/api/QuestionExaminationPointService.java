package com.lanking.uxb.service.resources.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.common.resource.question.QuestionExaminationPoint;

/**
 * 题目与考点关系相关Service
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public interface QuestionExaminationPointService {
	/**
	 * 根据题目id查找考点
	 *
	 * @param questionIds
	 *            题目id列表
	 * @return {@link List}
	 */
	List<QuestionExaminationPoint> findByQuestions(Collection<Long> questionIds);

	/**
	 * 查找单个题目对应的考点
	 *
	 * @param questionId
	 *            题目id
	 * @return {@link List}
	 */
	List<QuestionExaminationPoint> findByQuestion(long questionId);
}
