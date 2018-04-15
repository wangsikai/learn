package com.lanking.uxb.service.user.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.account.AccountPasswordQuestion;

/**
 * 提供账号密码相关操作api
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年2月6日
 */
public interface AccountPasswordQuestionService {

	/**
	 * 获取一个账户的密保问题
	 * 
	 * @param accountId
	 *            账号ID
	 * @return 密保问题列表
	 */
	List<AccountPasswordQuestion> findByAccountId(long accountId);

	/**
	 * 根据账号ID删除密保问题
	 * 
	 * @param accountId
	 *            账号ID
	 */
	void deleteByAccount(long accountId);

	/**
	 * 创建密保问题
	 * 
	 * @param apqs
	 *            密保问题信息
	 */
	void create(List<AccountPasswordQuestion> apqs);
}
