package com.lanking.uxb.service.user.api.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.user.Parent;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.NotImplementedException;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.api.UserTypeService;
import com.lanking.uxb.service.user.form.RegisterForm;

public abstract class AbstractUserService implements UserService, UserTypeService {

	@Autowired
	@Qualifier("UserRepo")
	Repo<User, Long> userRepo;
	@Autowired
	@Qualifier("TeacherRepo")
	Repo<Teacher, Long> teacherRepo;
	@Autowired
	@Qualifier("StudentRepo")
	Repo<Student, Long> studentRepo;
	@Autowired
	@Qualifier("ParentRepo")
	Repo<Parent, Long> parentRepo;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Transactional
	@Override
	public User createUser(Account account, RegisterForm form) {
		User user = new User();
		user.setAccountId(account.getId());
		user.setUserType(form.getType());
		user.setName(account.getName());
		user.setNickname(account.getName());
		if (StringUtils.isNotBlank(form.getRealName())) {
			user.setName(form.getRealName());
		}
		if (StringUtils.isNotBlank(form.getNickname())) {
			user.setNickname(form.getNickname());
		}
		if (StringUtils.isNotBlank(form.getRealName()) && StringUtils.isBlank(form.getNickname())) {
			user.setName(form.getRealName());
			user.setNickname(form.getRealName());
		}
		if (StringUtils.isBlank(form.getRealName()) && StringUtils.isNotBlank(form.getNickname())) {
			user.setName(form.getNickname());
			user.setNickname(form.getNickname());
		}
		return user;
	}

	@Transactional
	@Override
	public User createUser2(Account account, RegisterForm form) {
		User user = new User();
		user.setAccountId(account.getId());
		user.setUserType(form.getType());
		user.setName(account.getName());
		user.setNickname(account.getName());
		if (StringUtils.isNotBlank(form.getRealName())) {
			user.setName(form.getRealName());
		}
		if (StringUtils.isNotBlank(form.getNickname())) {
			user.setNickname(form.getNickname());
		}
		if (StringUtils.isNotBlank(form.getRealName()) && StringUtils.isBlank(form.getNickname())) {
			user.setName(form.getRealName());
			user.setNickname(form.getRealName());
		}
		if (StringUtils.isBlank(form.getRealName()) && StringUtils.isNotBlank(form.getNickname())) {
			user.setName(form.getNickname());
			user.setNickname(form.getNickname());
		}
		if (form.getChannelCode() != null) {
			user.setUserChannelCode(form.getChannelCode());
		}
		return user;
	}

	@Transactional(readOnly = true)
	@Override
	public User get(Long id) {
		return userRepo.get(id);
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, User> getUsers(Set<Long> ids) {
		return userRepo.mget(ids);
	}

	@Transactional(readOnly = true)
	@Override
	public UserInfo getUser(UserType userType, long userId) {
		return userService.getUser(userType, userId);
	}

	@Transactional
	@Override
	public UserInfo updateAvatar(UserType userType, long userId, long avatarId) {
		return userService.updateAvatar(userType, userId, avatarId);
	}

	@Transactional(readOnly = true)
	@Override
	public CursorPage<Long, User> queryUser(Set<Long> excludeUserIds, String name, CursorPageable<Long> cpr) {
		Params params = Params.param();
		if (CollectionUtils.isNotEmpty(excludeUserIds)) {
			params.put("excludeUserIds", excludeUserIds);
		}
		if (StringUtils.isNotBlank(name)) {
			params.put("name", "%" + name + "%");
		}
		return userRepo.find("$queryUser", params).fetch(cpr);
	}

	@Transactional
	@Override
	public User updateUsername(long id, String name) {
		User u = userRepo.get(id);
		u.setName(name);
		return userRepo.save(u);
	}

	@Transactional
	@Override
	public User updateNickname(long id, String nickname) {
		User u = userRepo.get(id);
		u.setNickname(nickname);
		return userRepo.save(u);
	}

	@Override
	public CursorPage<Long, User> getAllUser(CursorPageable<Long> cursor) {
		throw new NotImplementedException();
	}

	@Transactional
	@Override
	public void updateUserChannel(long userId, int channelCode) {
		User user = userRepo.get(userId);
		user.setUserChannelCode(channelCode);
		userRepo.save(user);
	}
}
