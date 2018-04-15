package com.lanking.uxb.channelSales.openmember.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.account.Account;

/**
 * 渠道管理平台账户管理Service
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
public interface CsAccountService {
	/**
	 * 根据id获得账户名
	 *
	 * @param id
	 *            账户id
	 * @return {@link Account}
	 */
	Account get(long id);

	/**
	 * mget账户数据
	 *
	 * @param ids
	 *            账户id列表
	 * @return {@link Map}
	 */
	Map<Long, Account> mget(Collection<Long> ids);

	/**
	 * 根据账户名称列表查找数据
	 *
	 * @param names
	 *            账户名列表
	 * @return id -> 数据
	 */
	Map<Long, Account> mgetByNames(Collection<String> names);
}
