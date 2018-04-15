package com.lanking.uxb.zycon.operation.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.account.Account;

/**
 * Ushuxue 管控台账户相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年9月22日 下午8:21:00
 */
public interface ZycAccountService {

	Account getAccount(long accountId);

	Map<Long, Account> mgeAccount(Collection<Long> keys);

	/**
	 * 禁用帐号
	 * 
	 * @param accountId
	 */
	void forbiddenAccount(Long accountId);

	Account getAccountByUserId(long userId);

	Map<Long, Account> mgetByUserId(Collection<Long> userIds);

}