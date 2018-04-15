package com.lanking.uxb.zycon.holiday.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemAnswer;

/**
 * 学生假期作业Service
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
public interface ZycHolidayStuHomeworkItemAnswerService {
	/**
	 * 根据学生专项作业题目id获得此题学生答案
	 *
	 * @param stuHomeworkItemQuestionId
	 *            学生专项作业题目id
	 * @return {@link HolidayStuHomeworkItemAnswer}
	 */
	List<HolidayStuHomeworkItemAnswer> query(long stuHomeworkItemQuestionId);

	/**
	 * 批量获得假期作业学生答案
	 *
	 * @param stuHomeworkItemQuestionIds
	 *            假期作业题目id
	 * @return {@link HolidayStuHomeworkItemAnswer}
	 */
	List<HolidayStuHomeworkItemAnswer> mgetListByQuestion(Collection<Long> stuHomeworkItemQuestionIds);
}
