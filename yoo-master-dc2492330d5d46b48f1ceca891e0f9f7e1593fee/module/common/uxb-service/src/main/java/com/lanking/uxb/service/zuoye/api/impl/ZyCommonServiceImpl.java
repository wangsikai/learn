package com.lanking.uxb.service.zuoye.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.uxb.service.zuoye.api.ZyCommonService;

@Transactional(readOnly = true)
@Service
public class ZyCommonServiceImpl implements ZyCommonService {

	@Autowired
	@Qualifier("UserRepo")
	Repo<User, Long> userRepo;
	@Autowired
	@Qualifier("StudentHomeworkRepo")
	Repo<StudentHomework, Long> stuHkRepo;

	@Override
	public int getUsers() {
		return userRepo.find("getAllUserCount").get(Integer.class);
	}

	@Override
	public int getHomeWorks() {
		return stuHkRepo.find("getAllHkCount").get(Integer.class);
	}

}
