package com.lanking.uxb.zycon.homework.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.user.User;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
public interface ZycUserService {
	/**
	 * 根据id查询数据
	 *
	 * @param id
	 *            id
	 * @return 数据
	 */
	User get(Long id);

	/**
	 * mgetList User
	 *
	 * @param ids
	 *            ids
	 * @return 数据
	 */
	List<User> mgetList(Collection<Long> ids);

	/**
	 * mget user
	 *
	 * @param ids
	 *            用户id
	 * @return {@link User}
	 */
	Map<Long, User> mget(Collection<Long> ids);
}
