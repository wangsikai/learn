package com.lanking.uxb.service.holiday.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkAnswerImage;

/**
 * 假期多张图片Service
 *
 * @author xinyu.zhou
 * @since 3.9.0
 */
public interface HolidayStuHomeworkAnswerImageService {

	/**
	 * 根据专项题目id查找答题过程图片
	 *
	 * @param stuItemQuestionId
	 *            假期专项题目id
	 * @return {@link List}
	 */
	List<HolidayStuHomeworkAnswerImage> findByItemQuestion(Long stuItemQuestionId);

	/**
	 * 根据假期
	 *
	 * @param stuItemQuestionIds
	 *            假期传项题目id列表
	 * @return {@link Map}
	 */
	Map<Long, List<HolidayStuHomeworkAnswerImage>> findByItemQuestions(Collection<Long> stuItemQuestionIds);
}
