package com.lanking.uxb.service.doQuestion.api;

import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoal;
import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoalCount;
import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoalLevel;

/**
 * 做题目标相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月28日
 */
public interface DoQuestionGoalService {

	DoQuestionGoal findByUserId(long userId);

	void setDoQuestionGoal(long userId, DoQuestionGoalLevel level, int goal);

	DoQuestionGoalCount findByUserId(long userId, long date0);

	void incrDoQuestionGoalCount(long userId, long date0, int delta);
}
