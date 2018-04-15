package com.lanking.uxb.service.school.api;

/**
 * @author xinyu.zhou
 * @since 2.6.0
 */
public interface SchoolQuestionService {
	/**
	 * 统计学校题目
	 *
	 * @param schoolId
	 *            学校id
	 * @return 学校题目数量
	 */
	long countBySchool(long schoolId);
}
