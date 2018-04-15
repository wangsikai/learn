package com.lanking.uxb.channelSales.channel.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.channelSales.channel.form.ChannelUserQueryForm;

/**
 * 渠道平台用户信息Service
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
public interface CsUserService {
	/**
	 * 获得用户
	 *
	 * @param id
	 *            用户id
	 * @return {@link User}
	 */
	User get(long id);

	/**
	 * mget用户数据
	 *
	 * @param ids
	 *            用户id列表
	 * @return {@link Map}
	 */
	Map<Long, User> mget(Collection<Long> ids);

	/**
	 * 查找渠道商学校的用户数据
	 *
	 * @param schoolId
	 *            学校id
	 * @param userType
	 *            {@link UserType}
	 * @return {@link List}
	 */
	List<User> findBySchool(long schoolId, UserType userType);

	/**
	 * 获得用户列表
	 *
	 * @param ids
	 *            用户列表id
	 * @return {@link List}
	 */
	List<User> mgetList(Collection<Long> ids);

	/**
	 * 查找渠道商的用户数据
	 *
	 * @param query
	 *            查询条件
	 * @return {@link List}
	 */
	Page<Map> findByChannel(ChannelUserQueryForm query, Pageable p);

	/**
	 * 根据用户名列表查找数据
	 *
	 * @param names
	 *            用户名列表
	 * @return 用户列表
	 */
	List<User> findByAccountNames(List<String> names);
}
