package com.lanking.uxb.service.user.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.user.api.UserChannelService;

@Service
public class UserChannelServiceImpl implements UserChannelService {

	@Autowired
	@Qualifier("UserChannelRepo")
	Repo<UserChannel, Integer> userChannelRepo;

	@Transactional(readOnly = true)
	@Override
	public UserChannel findByCode(int code) {
		return userChannelRepo.find("$findByCode", Params.param("code", code)).get();
	}

	@Transactional(readOnly = true)
	@Override
	public UserChannel findByName(String name) {
		return userChannelRepo.find("$findByName", Params.param("name", name)).get();
	}

}
