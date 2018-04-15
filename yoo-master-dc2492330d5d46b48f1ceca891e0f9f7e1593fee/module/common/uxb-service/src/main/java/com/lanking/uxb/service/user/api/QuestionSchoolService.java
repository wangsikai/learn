package com.lanking.uxb.service.user.api;

import com.lanking.cloud.domain.yoomath.school.QuestionSchool;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
public interface QuestionSchoolService {
	QuestionSchool save(QuestionSchool questionSchool);

	/**
	 * 根据学校id获得数据
	 *
	 * @since 2.6.0
	 * @param schoolId
	 *            学校id
	 * @return {@link QuestionSchool}
	 */
	QuestionSchool getBySchool(long schoolId);
}
