package com.lanking.uxb.service.zuoye.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.homework.Homework;

/**
 * 批改人相关接口
 * 
 * @since yoomath V1.4
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月21日
 */
public interface ZyCorrectUserService {

	/**
	 * 获取所有批改人的手机号码(SQL里面有数量限制)
	 * 
	 * @since yoomath V1.4
	 * @return 手机号码列表
	 */
	List<String> getAllMobile();

	/**
	 * 异步通知相关人员
	 * 
	 * @since yoomath V1.4
	 * @param homeworkId
	 *            作业ID
	 */
	void asyncNoticeUser(long homeworkId);

	/**
	 * 有作业提交后异步通知相关人员
	 */
	void asyncNoticeUserAfterCommit(long homeworkId);

	/**
	 * 被自动提交后异步通知相关人员
	 * 
	 * @since yoomath V1.4.1
	 * @param homeworkId
	 *            作业ID
	 * @param autoCommit
	 *            自动提交的数量
	 */
	void asyncNoticeUserAfterAutoCommit(long homeworkId, int autoCommit);

	/**
	 * 作业被自动提交前十分钟提醒
	 * 
	 * @since yoomath V1.4.1
	 * @param hk
	 *            作业
	 */
	void noticeUserBeforeAutoCommit(Homework hk);

}
