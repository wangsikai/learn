package com.lanking.uxb.zycon.homework.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.uxb.zycon.homework.api.ZycUserService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
@Service
@Transactional(readOnly = true)
public class ZycUserServiceImpl implements ZycUserService {

	@Autowired
	@Qualifier("UserRepo")
	private Repo<User, Long> repo;

	@Override
	public User get(Long id) {
		return repo.get(id);
	}

	public List<User> mgetList(Collection<Long> ids) {
		return repo.mgetList(ids);
	}

	@Override
	public Map<Long, User> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}
}
