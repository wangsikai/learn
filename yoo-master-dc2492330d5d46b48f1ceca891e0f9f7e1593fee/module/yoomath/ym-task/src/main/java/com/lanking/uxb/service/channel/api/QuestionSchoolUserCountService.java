package com.lanking.uxb.service.channel.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.school.QuestionSchool;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 题库学校人数统计
 *
 * @author zemin.song
 * @since 2.5.0
 */
public interface QuestionSchoolUserCountService {
	/**
	 * 学校 用户统计
	 */
	void staticSchoolUserCount();

	/**
	 * 查询题库学校
	 * 
	 * @param status
	 *            状态值
	 * @return List<QuestionSchool>
	 */
	List<QuestionSchool> findAllQuestionSchool(Status status);

	/**
	 * 查询题库学校
	 * 
	 * @return List<QuestionSchool>
	 */
	List<QuestionSchool> findAllQuestionSchool();

}
