package com.lanking.uxb.zycon.operation.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.console.common.CorrectUser;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.zycon.operation.api.ZycCorrectUserService;

@Transactional(readOnly = true)
@Service
public class ZycCorrectUserServiceImpl implements ZycCorrectUserService {

	@Autowired
	@Qualifier("CorrectUserRepo")
	Repo<CorrectUser, Long> correctUserRepo;

	@Transactional
	@Override
	public CorrectUser add(String name, String mobile) {
		CorrectUser cu = new CorrectUser();
		cu.setName(name);
		cu.setMobile(mobile);
		cu.setStatus(Status.ENABLED);
		return correctUserRepo.save(cu);
	}

	@Transactional
	@Override
	public CorrectUser edit(Long id, Status status) {
		CorrectUser cu = correctUserRepo.get(id);
		cu.setStatus(status);
		return correctUserRepo.save(cu);
	}

	@Override
	public List<CorrectUser> list() {
		return correctUserRepo.find("$list").list();
	}

}
