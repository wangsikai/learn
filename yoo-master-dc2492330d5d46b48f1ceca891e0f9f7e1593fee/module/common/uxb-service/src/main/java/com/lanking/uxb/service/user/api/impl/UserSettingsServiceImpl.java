package com.lanking.uxb.service.user.api.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.UserSettings;
import com.lanking.uxb.service.user.api.UserSettingsService;

@Transactional(readOnly = true)
@Service
public class UserSettingsServiceImpl implements UserSettingsService {

	@Autowired
	@Qualifier("UserSettingsRepo")
	private Repo<UserSettings, Long> userSettingsRepo;

	@Override
	public UserSettings get(long userId) {
		return userSettingsRepo.get(userId);
	}

	@Override
	public UserSettings safeGet(long userId) {
		UserSettings settings = userSettingsRepo.get(userId);
		if (settings == null) {
			settings = new UserSettings();
			settings.setId(userId);
		}
		return settings;
	}

	@Override
	public Map<Long, UserSettings> safeGets(Collection<Long> userIds) {
		Map<Long, UserSettings> userSettings = userSettingsRepo.mget(userIds);
		for (Long userId : userIds) {
			if (userSettings.get(userId) == null) {
				UserSettings settings = new UserSettings();
				settings.setId(userId);
				userSettings.put(userId, settings);
			}
		}
		return userSettings;
	}

}
