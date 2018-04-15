package com.lanking.uxb.service.resources.api;

import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.homework.AppealType;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppeal;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppealStatus;

/**
 * 提供题目批改错误相关接口
 * 
 * @author <a href="mailto:qiuxue.jiang@elanking.com">qiuxue.jiang</a>
 * @version 2018年2月9日
 */
public interface QuestionAppealService {
	/**
	 * 学生作业题目id查询申述记录
	 * 
	 */
	QuestionAppeal getAppeal(Long sHkQId);

	/**
	 * 添加一条申述记录
	 * 
	 * @param id
	 *            id,目前暂时为学生作业题目id，来源不同id含义不同
	 * @param source
	 *            来源，0 作业 1 订正题 2 自主练习
	 * @param type
	 *            申述类型 0 非申诉题 1 批改错误 2 习题错误
	 * @param comment
	 *            申述描述
	 * @param userType
	 *            用户类型
	 */
	void addComment(AppealType type, Long id, Integer source, String comment, UserType userType);

	/**
	 * 更新申述记录的状态
	 * 
	 */
	void updateStatus(Long id, QuestionAppealStatus status);

	/**
	 * 获取学生作业习题最近的一条申诉记录.
	 * 
	 * @param sHkQId
	 *            学生作业
	 * @return
	 */
	QuestionAppeal getLastAppeal(long sHkQId);
}
