package com.lanking.uxb.service.user.api;

import java.util.Map;
import java.util.Set;

import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.uxb.service.user.form.RegisterForm;

/**
 * 用户相关接口(包括登陆注册相关)
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月9日
 *
 */
public interface UserService {

	User createUser(Account account, RegisterForm form);

	/**
	 * 创建用户
	 * 
	 * @since 2.1
	 * @param account
	 *            账号信息
	 * @param form
	 *            注册信息
	 * @return {@link User}
	 */
	User createUser2(Account account, RegisterForm form);

	User get(Long id);

	Map<Long, User> getUsers(Set<Long> ids);

	UserInfo getUser(long userId);

	Map<Long, UserInfo> getUserInfos(UserType type, Set<Long> ids);

	UserInfo getUser(UserType userType, long userId);

	UserInfo updateAvatar(long userId, long avatarId);

	UserInfo updateAvatar(UserType userType, long userId, long avatarId);

	/**
	 * 搜索用户
	 * 
	 * @param excludeUserIds
	 *            排除者用户IDs
	 * @param name
	 *            用户名(支持模糊搜索)
	 * @param cpr
	 *            分页条件
	 * @return 搜素结果
	 */
	CursorPage<Long, User> queryUser(Set<Long> excludeUserIds, String name, CursorPageable<Long> cpr);

	User updateUsername(long id, String name);

	User updateNickname(long id, String nickname);

	/**
	 * 更新签名介绍
	 * 
	 * @since 2.1
	 * @param userType
	 *            用户类型
	 * @param userId
	 *            用户ID
	 * @param introduce
	 *            签名介绍
	 */
	void updateIntroduce(UserType userType, long userId, String introduce);

	/**
	 * 游标方式 获取所有用户
	 * 
	 * @param cursor
	 *            游标
	 * @return 用户PAGE
	 */
	CursorPage<Long, User> getAllUser(CursorPageable<Long> cursor);

	/**
	 * 更新用户渠道.
	 * 
	 * @param userId
	 *            用户ID
	 * @param channelCode
	 *            渠道号
	 */
	void updateUserChannel(long userId, int channelCode);

	/**
	 * 更新用户学校.
	 * 
	 * @param userType
	 *            用户类型
	 * @param userId
	 *            用户ID
	 * @param schoolId
	 *            学校ID
	 */
	void updateSchool(UserType userType, long userId, long schoolId);
}
