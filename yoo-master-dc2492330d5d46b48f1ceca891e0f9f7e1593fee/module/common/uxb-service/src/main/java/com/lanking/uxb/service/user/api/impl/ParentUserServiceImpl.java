package com.lanking.uxb.service.user.api.impl;

import java.util.Map;
import java.util.Set;

import javax.persistence.MappedSuperclass;

import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.user.Parent;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.user.form.RegisterForm;

//@Service("pUserService")
@MappedSuperclass
@Transactional(readOnly = true)
public class ParentUserServiceImpl extends AbstractUserService {

	@Override
	public UserType getType() {
		return UserType.PARENT;
	}

	@Override
	public boolean accept(UserType userType) {
		return userType == UserType.PARENT;
	}

	@Transactional
	@Override
	public User createUser(Account account, RegisterForm form) {
		return null;
	}

	@Transactional
	@Override
	public User createUser2(Account account, RegisterForm form) {
		return null;
	}

	@Override
	public UserInfo getUser(long userId) {
		Parent parent = parentRepo.get(userId);
		if (parent != null) {
			parent.setUserType(UserType.PARENT);
		}
		return parent;
	}

	@Transactional
	@Override
	public UserInfo updateAvatar(long userId, long avatarId) {
		Parent parent = parentRepo.get(userId);
		parent.setAvatarId(avatarId);
		parentRepo.save(parent);
		return parent;
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, UserInfo> getUserInfos(UserType type, Set<Long> ids) {
		Map<Long, UserInfo> userInfos = Maps.newHashMap();
		Map<Long, Parent> parents = parentRepo.mget(ids);
		for (Long key : parents.keySet()) {
			Parent p = parents.get(key);
			p.setUserType(UserType.PARENT);
			userInfos.put(key, p);
		}
		return userInfos;
	}

	@Transactional
	@Override
	public void updateIntroduce(UserType userType, long userId, String introduce) {
		parentRepo.execute("updateIntroduce", Params.param("userId", userId).put("introduce", introduce));
	}

	@Override
	public void updateSchool(UserType userType, long userId, long schoolId) {
		// ...
	}
}
