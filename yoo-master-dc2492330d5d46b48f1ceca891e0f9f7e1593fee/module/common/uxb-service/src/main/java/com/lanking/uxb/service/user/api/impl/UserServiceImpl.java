package com.lanking.uxb.service.user.api.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.api.UserTypeService;
import com.lanking.uxb.service.user.form.RegisterForm;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月9日
 *
 */
@Service("userService")
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, ApplicationContextAware {

	@Autowired
	@Qualifier("UserRepo")
	private Repo<User, Long> userRepo;
	private ApplicationContext appContext;

	private Map<UserType, UserService> userServices = Maps.newHashMap();

	private UserService defUserService;
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;

	@Transactional
	@Override
	public User createUser(Account account, RegisterForm form) {
		return userServices.get(form.getType()).createUser(account, form);
	}

	@Transactional
	@Override
	public User createUser2(Account account, RegisterForm form) {
		return userServices.get(form.getType()).createUser2(account, form);
	}

	@Override
	@Transactional(readOnly = true)
	public User get(Long id) {
		return defUserService.get(id);
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, User> getUsers(Set<Long> ids) {
		return defUserService.getUsers(ids);
	}

	@Transactional(readOnly = true)
	@Override
	public UserInfo getUser(long userId) {
		User user = accountService.getUserByUserId(userId);
		if (user == null) {
			return null;
		}
		return userServices.get(user.getUserType()).getUser(userId);
	}

	@Transactional(readOnly = true)
	@Override
	public UserInfo getUser(UserType userType, long userId) {
		return userServices.get(userType).getUser(userId);
	}

	@Transactional
	@Override
	public UserInfo updateAvatar(long userId, long avatarId) {
		User user = accountService.getUserByUserId(userId);
		return userServices.get(user.getUserType()).updateAvatar(userId, avatarId);
	}

	@Transactional
	@Override
	public UserInfo updateAvatar(UserType userType, long userId, long avatarId) {
		return userServices.get(userType).updateAvatar(userId, avatarId);
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, UserInfo> getUserInfos(UserType type, Set<Long> ids) {
		return userServices.get(type).getUserInfos(type, ids);
	}

	@Transactional(readOnly = true)
	@Override
	public CursorPage<Long, User> queryUser(Set<Long> excludeUserIds, String name, CursorPageable<Long> cpr) {
		return defUserService.queryUser(excludeUserIds, name, cpr);
	}

	@Transactional
	@Override
	public User updateUsername(long id, String name) {
		return defUserService.updateUsername(id, name);
	}

	@Transactional
	@Override
	public User updateNickname(long id, String nickname) {
		return defUserService.updateNickname(id, nickname);
	}

	@Transactional
	@Override
	public void updateIntroduce(UserType userType, long userId, String introduce) {
		userServices.get(userType).updateIntroduce(userType, userId, introduce);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
		for (UserService userService : appContext.getBeansOfType(UserService.class).values()) {
			if (userService instanceof UserTypeService) {
				if (defUserService == null) {
					defUserService = userService;
				}
				userServices.put(((UserTypeService) userService).getType(), userService);
			}
		}
	}

	@Override
	public CursorPage<Long, User> getAllUser(CursorPageable<Long> cursor) {
		return userRepo.find("$getAllUserByPage").fetch(cursor);
	}

	@Override
	@Transactional
	public void updateSchool(UserType userType, long userId, long schoolId) {
		userServices.get(userType).updateSchool(userType, userId, schoolId);
	}

	@Override
	@Transactional
	public void updateUserChannel(long userId, int channelCode) {
		defUserService.updateUserChannel(userId, channelCode);
	}
}
