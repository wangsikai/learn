package com.lanking.uxb.service.resources.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswerImage;

/**
 * 学生答题图片Service
 *
 * @author xinyu.zhou
 * @since 3.0.3
 */
public interface StudentHomeworkAnswerImageService {
	/**
	 * 返回StudentHomeworkQuestion.id -> 学生答题图片相关数据
	 *
	 * @param stuHkQuestionIds
	 *            StudentHomeworkQuestion.id
	 * @return {@link Map}
	 */
	Map<Long, List<StudentHomeworkAnswerImage>> mgetByStuHKQuestion(Collection<Long> stuHkQuestionIds);

	/**
	 * 根据学生StudentHomeworkQuestion的id返回这个学生这一题的答题图片数据
	 *
	 * @param stuHkQuestionId
	 *            StudentHomeworkQuestion.id
	 * @return {@link List}
	 */
	List<StudentHomeworkAnswerImage> findByStuHkQuestion(long stuHkQuestionId);


}
