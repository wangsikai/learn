package com.lanking.uxb.channelSales.openmember.api;

/**
 * @author xinyu.zhou
 * @since 2.6.0
 */
public interface CsQuestionSchoolService {
	/**
	 * 创建QuestionSchool对象
	 *
	 * @param schoolId
	 *            学校id
	 * @param users
	 *            用户数量
	 */
	void create(long schoolId, int users);
}
