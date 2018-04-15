package com.lanking.uxb.zycon.task.api;

import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoal;

/**
 * @author xinyu.zhou
 * @since 4.1.0
 */
public interface ZycDoQuestionGoalService {
	/**
	 * 查询用户的学习目标设置情况
	 *
	 * @param userId
	 *            用户id
	 * @return {@link DoQuestionGoal}
	 */
	DoQuestionGoal findByUserId(long userId);
}
