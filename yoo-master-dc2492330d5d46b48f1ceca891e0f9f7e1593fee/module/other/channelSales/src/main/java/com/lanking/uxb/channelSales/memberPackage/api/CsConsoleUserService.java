package com.lanking.uxb.channelSales.memberPackage.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.domain.yoo.account.Account;

/**
 * 渠道管理员service
 *
 * @author zemin.song
 * @version 2016年11月15日
 */
public interface CsConsoleUserService {

	/**
	 * 根据id获得账户名
	 *
	 * @param id
	 *            账户id
	 * @return {@link Account}
	 */
	ConsoleUser get(long id);

	/**
	 * mget账户数据
	 *
	 * @param ids
	 *            账户id列表
	 * @return {@link Map}
	 */
	Map<Long, ConsoleUser> mget(Collection<Long> ids);

}
