package com.lanking.uxb.zycon.qs.api;

import com.lanking.cloud.domain.yoomath.school.QuestionSchool;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 校本题库处理Service
 *
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
public interface ZycQuestionSchoolService {
	QuestionSchool get(long id);

	/**
	 * 分页查询学校
	 *
	 * @param pageable
	 *            {@link Pageable}
	 * @param schoolName
	 *            学校名称
	 * @return 分页数据
	 */
	Page<QuestionSchool> page(Pageable pageable, String schoolName);

	/**
	 * 启用或禁用学校
	 *
	 * @param schoolId
	 *            id
	 * @param status
	 *            {@link Status}
	 * @return {@link QuestionSchool}
	 */
	QuestionSchool update(long schoolId, Status status);

	/**
	 * 增加校本题目数量
	 *
	 * @param schoolId
	 *            schoolId
	 * @param count
	 *            增加的数量
	 * @return {@link QuestionSchool}
	 */
	QuestionSchool incrQuestionCount(long schoolId, long count);

	/**
	 * 创建一个校本学校
	 *
	 * @param schoolId
	 *            学校id
	 * @return {@link QuestionSchool}
	 */
	QuestionSchool create(long schoolId);

	/**
	 * 更新校本学校可以免费录入的题目数量
	 * 
	 * @param schoolId
	 * @param count
	 */
	void updateRecordQuestionCount(Long schoolId, Long count);
}
