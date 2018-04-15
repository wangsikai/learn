package com.lanking.uxb.service.imperial.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityStudent;

/**
 * 学生用户报名信息.
 * 
 * @author peng.zhao
 * @version 2017年11月9日
 */
public interface ImperialExaminationActivityStudentService {

	/**
	 * 报名科举考试
	 * 
	 * @param code
	 *            活动code
	 * @param userId
	 *            用户id
	 */
	void signUp(long code, long userId);

	/**
	 * 获取用户报名数据.
	 * 
	 * @param code
	 *            活动code
	 * @param userId
	 *            用户ID.
	 * @return
	 */
	ImperialExaminationActivityStudent getUser(long code, long userId);

	/**
	 * 查询指定学生用户是否已经报名
	 * 
	 * @param code
	 *            活动code
	 * @param userIds
	 *            学生id列表
	 * @return
	 */
	List<Long> getUsers(long code, List<Long> userIds);

	/**
	 * 报名科举考试
	 * 
	 * @param code
	 *            活动code
	 * @param userIds
	 *            学生id列表
	 */
	void signUpByUsers(long code, List<Long> userIds);
}
